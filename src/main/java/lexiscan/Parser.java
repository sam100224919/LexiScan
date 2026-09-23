package lexiscan;

import lexiscan.ast.BinaryExpr;
import lexiscan.ast.Expr;
import lexiscan.ast.ExprStmt;
import lexiscan.ast.LiteralExpr;
import lexiscan.ast.VariableExpr;
import lexiscan.ast.Stmt;
import lexiscan.ast.VarStmt;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    // Entry point
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

    // statement -> variableDeclaration | expressionStatement
    private Stmt statement() {

        // let name = expression;
        if (match(TokenType.LET)) {
            return variableDeclaration();
        }

        return expressionStatement();
    }

    // let name = expression ;
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

    // expression ;
    private Stmt expressionStatement() {

        Expr expr = expression();

        consume(
                TokenType.SEMICOLON,
                "Expected ';' after expression."
        );

        return new ExprStmt(expr);
    }

    // expression
    private Expr expression() {
        return addition();
    }

    // addition -> multiplication (("+" | "-") multiplication)*
    private Expr addition() {

        Expr expr = multiplication();

        while (match(TokenType.PLUS, TokenType.MINUS)) {

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

    // multiplication -> primary (("*" | "/") primary)*
    private Expr multiplication() {

        Expr expr = primary();

        while (match(TokenType.STAR, TokenType.SLASH)) {

            Token operator = previous();

            Expr right = primary();

            expr = new BinaryExpr(
                    expr,
                    operator,
                    right
            );
        }

        return expr;
    }

    // primary -> NUMBER | IDENTIFIER | "(" expression ")"
    private Expr primary() {

        // Number
        if (match(TokenType.NUMBER)) {
            return new LiteralExpr(previous().literal());
        }

        // Variable
        if (match(TokenType.IDENTIFIER)) {
            return new VariableExpr(previous());
        }

        // Parentheses
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

    // ------------------------------------------------------------
    // Helper methods
    // ------------------------------------------------------------

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

        throw error(peek(), message);
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
                message + " Found '" +
                        token.lexeme() +
                        "'."
        );
    }
}