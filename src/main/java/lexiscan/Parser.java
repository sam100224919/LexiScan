package lexiscan;

import lexiscan.ast.BinaryExpr;
import lexiscan.ast.BlockStmt;
import lexiscan.ast.CallExpr;
import lexiscan.ast.Expr;
import lexiscan.ast.ExprStmt;
import lexiscan.ast.FunctionStmt;
import lexiscan.ast.IfStmt;
import lexiscan.ast.LiteralExpr;
import lexiscan.ast.ReturnStmt;
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

            statements.add(declaration());
        }

        return statements;
    }

    private Stmt declaration() {

        if (match(TokenType.FUN)) {
            return functionDeclaration();
        }

        if (match(TokenType.LET)) {
            return variableDeclaration();
        }

        return statement();
    }

    private Stmt statement() {

        if (match(TokenType.RETURN)) {
            return returnStatement();
        }

        if (match(TokenType.WHILE)) {
            return whileStatement();
        }

        if (match(TokenType.IF)) {
            return ifStatement();
        }

        if (match(TokenType.LEFT_BRACE)) {
            return block();
        }

        return expressionStatement();
    }

    private Stmt functionDeclaration() {

        Token name = consume(
                TokenType.IDENTIFIER,
                "Expected function name."
        );

        consume(
                TokenType.LEFT_PAREN,
                "Expected '(' after function name."
        );

        List<Token> parameters = new ArrayList<>();

        if (!check(TokenType.RIGHT_PAREN)) {
            do {
                parameters.add(
                        consume(
                                TokenType.IDENTIFIER,
                                "Expected parameter name."
                        )
                );
            } while (match(TokenType.COMMA));
        }

        consume(
                TokenType.RIGHT_PAREN,
                "Expected ')' after parameters."
        );

        consume(
                TokenType.LEFT_BRACE,
                "Expected '{' before function body."
        );

        List<Stmt> body = blockStatements();

        return new FunctionStmt(
                name,
                parameters,
                body
        );
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

        return new BlockStmt(
                blockStatements()
        );
    }

    private List<Stmt> blockStatements() {

        List<Stmt> statements = new ArrayList<>();

        while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            statements.add(declaration());
        }

        consume(
                TokenType.RIGHT_BRACE,
                "Expected '}' after block."
        );

        return statements;
    }

    private Stmt returnStatement() {

        Token keyword = previous();

        Expr value = null;

        if (!check(TokenType.SEMICOLON)) {
            value = expression();
        }

        consume(
                TokenType.SEMICOLON,
                "Expected ';' after return value."
        );

        return new ReturnStmt(keyword, value);
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

        return call();
    }

    private Expr call() {

        Expr expr = primary();

        while (true) {

            if (match(TokenType.LEFT_PAREN)) {
                expr = finishCall(expr);
            } else {
                break;
            }
        }

        return expr;
    }

    private Expr finishCall(Expr callee) {

        List<Expr> arguments = new ArrayList<>();

        if (!check(TokenType.RIGHT_PAREN)) {
            do {
                arguments.add(expression());
            } while (match(TokenType.COMMA));
        }

        Token paren = consume(
                TokenType.RIGHT_PAREN,
                "Expected ')' after arguments."
        );

        return new CallExpr(
                callee,
                paren,
                arguments
        );
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