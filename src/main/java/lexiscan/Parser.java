package lexiscan;

import lexiscan.ast.BinaryExpr;
import lexiscan.ast.BlockStmt;
import lexiscan.ast.Expr;
import lexiscan.ast.ExprStmt;
import lexiscan.ast.IfStmt;
import lexiscan.ast.LiteralExpr;
import lexiscan.ast.Stmt;
import lexiscan.ast.UnaryExpr;
import lexiscan.ast.VarStmt;
import lexiscan.ast.VariableExpr;
import lexiscan.ast.WhileStmt;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Stmt> parse() {

        List<Stmt> statements = new ArrayList<>();

        while (!isAtEnd()) {

            if (match(TokenType.SEMICOLON)) {
                continue;
            }

            statements.add(statement());
        }

        return statements;
    }

    private Stmt statement() {

        if (match(TokenType.WHILE)) {
            return whileStatement();
        }

        if (match(TokenType.IF)) {
            return ifStatement();
        }

        if (match(TokenType.LEFT_BRACE)) {
            return block();
        }

        if (match(TokenType.LET)) {
            return variableDeclaration();
        }

        return expressionStatement();
    }

    private Stmt whileStatement() {

        consume(
                TokenType.LEFT_PAREN,
                "Expected '(' after 'while'."
        );

        Expr condition = expression();

        consume(
                TokenType.RIGHT_PAREN,
                "Expected ')' after while condition."
        );

        Stmt body = statement();

        return new WhileStmt(
                condition,
                body
        );
    }

    private Stmt ifStatement() {

        consume(
                TokenType.LEFT_PAREN,
                "Expected '(' after 'if'."
        );

        Expr condition = expression();

        consume(
                TokenType.RIGHT_PAREN,
                "Expected ')' after if condition."
        );

        Stmt thenBranch = statement();

        Stmt elseBranch = null;

        if (match(TokenType.ELSE)) {
            elseBranch = statement();
        }

        return new IfStmt(
                condition,
                thenBranch,
                elseBranch
        );
    }

    private Stmt block() {

        List<Stmt> statements = new ArrayList<>();

        while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            statements.add(statement());
        }

        consume(
                TokenType.RIGHT_BRACE,
                "Expected '}' after block."
        );

        return new BlockStmt(statements);
    }

    private Stmt variableDeclaration() {

        Token name = consume(
                TokenType.IDENTIFIER,
                "Expected variable name."
        );

        consume(
                TokenType.EQUAL,
                "Expected '=' after variable name."
        );

        Expr initializer = expression();

        consume(
                TokenType.SEMICOLON,
                "Expected ';' after variable declaration."
        );

        return new VarStmt(name, initializer);
    }

    private Stmt expressionStatement() {

        Expr expr = expression();

        consume(
                TokenType.SEMICOLON,
                "Expected ';' after expression."
        );

        return new ExprStmt(expr);
    }

    // --------------------------------------------------
    // EXPRESSIONS
    // --------------------------------------------------

    private Expr expression() {
        return or();
    }

    private Expr or() {

        Expr expr = and();

        while (match(TokenType.OR)) {

            Token operator = previous();

            Expr right = and();

            expr = new BinaryExpr(
                    expr,
                    operator,
                    right
            );
        }

        return expr;
    }

    private Expr and() {

        Expr expr = equality();

        while (match(TokenType.AND)) {

            Token operator = previous();

            Expr right = equality();

            expr = new BinaryExpr(
                    expr,
                    operator,
                    right
            );
        }

        return expr;
    }

    /*
     * Equality has lower precedence than comparison.
     */
    private Expr equality() {

        Expr expr = comparison();

        while (match(
                TokenType.EQUAL_EQUAL,
                TokenType.BANG_EQUAL
        )) {

            Token operator = previous();

            Expr right = comparison();

            expr = new BinaryExpr(
                    expr,
                    operator,
                    right
            );
        }

        return expr;
    }

    /*
     * Comparison has lower precedence than addition.
     */
    private Expr comparison() {

        Expr expr = addition();

        while (match(
                TokenType.GREATER,
                TokenType.LESS
        )) {

            Token operator = previous();

            Expr right = addition();

            expr = new BinaryExpr(
                    expr,
                    operator,
                    right
            );
        }

        return expr;
    }

    /*
     * Addition has lower precedence than multiplication.
     *
     * Example:
     *
     * 2 + 3 * 4
     *
     * becomes:
     *
     * 2 + (3 * 4)
     */
    private Expr addition() {

        Expr expr = multiplication();

        while (match(
                TokenType.PLUS,
                TokenType.MINUS
        )) {

            Token operator = previous();

            Expr right = multiplication();

            expr = new BinaryExpr(
                    expr,
                    operator,
                    right
            );
        }

        return expr;
    }

    /*
     * Multiplication has higher precedence than addition.
     */
    private Expr multiplication() {

        Expr expr = unary();

        while (match(
                TokenType.STAR,
                TokenType.SLASH
        )) {

            Token operator = previous();

            Expr right = unary();

            expr = new BinaryExpr(
                    expr,
                    operator,
                    right
            );
        }

        return expr;
    }

    /*
     * Unary operators.
     *
     * Examples:
     *
     * -5
     * -10
     * -(5 + 3)
     */
    private Expr unary() {

        if (match(TokenType.BANG)) {

            Token operator = previous();

            Expr right = unary();

            return new UnaryExpr(
                    operator,
                    right
            );
        }

        if (match(TokenType.MINUS)) {

            Token operator = previous();

            Expr right = unary();

            return new BinaryExpr(
                    new LiteralExpr(0.0),
                    operator,
                    right
            );
        }

        return primary();
    }

    /*
     * Primary expressions are the highest-precedence
     * expressions.
     */
    private Expr primary() {

        if (match(TokenType.TRUE)) {
            return new LiteralExpr(true);
        }

        if (match(TokenType.FALSE)) {
            return new LiteralExpr(false);
        }

        if (match(TokenType.NUMBER)) {

            return new LiteralExpr(
                    previous().literal()
            );
        }

        if (match(TokenType.IDENTIFIER)) {

            return new VariableExpr(
                    previous()
            );
        }

        if (match(TokenType.LEFT_PAREN)) {

            Expr expr = expression();

            consume(
                    TokenType.RIGHT_PAREN,
                    "Expected ')' after expression."
            );

            return expr;
        }

        throw error(
                peek(),
                "Expected expression."
        );
    }

    // --------------------------------------------------
    // PARSER HELPERS
    // --------------------------------------------------

    private boolean match(TokenType... types) {

        for (TokenType type : types) {

            if (check(type)) {

                advance();

                return true;
            }
        }

        return false;
    }

    private Token consume(
            TokenType type,
            String message
    ) {

        if (check(type)) {
            return advance();
        }

        throw error(
                peek(),
                message
        );
    }

    private boolean check(TokenType type) {

        if (isAtEnd()) {
            return type == TokenType.EOF;
        }

        return peek().type() == type;
    }

    private Token advance() {

        if (!isAtEnd()) {
            current++;
        }

        return previous();
    }

    private boolean isAtEnd() {
        return peek().type() == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private RuntimeException error(
            Token token,
            String message
    ) {

        return new RuntimeException(
                message
                        + " Found '"
                        + token.lexeme()
                        + "'."
        );
    }
}