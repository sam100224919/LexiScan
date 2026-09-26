package lexiscan;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LexerTest {

    private List<Token> scan(String source) {
        return new Lexer(source).scanTokens();
    }

    private void assertToken(
            List<Token> tokens,
            int index,
            TokenType type,
            String lexeme,
            Object literal
    ) {
        assertEquals(type, tokens.get(index).getType());
        assertEquals(lexeme, tokens.get(index).getLexeme());
        assertEquals(literal, tokens.get(index).getLiteral());
    }

    private void assertEof(List<Token> tokens, int index) {
        assertEquals(TokenType.EOF, tokens.get(index).getType());
        assertEquals("", tokens.get(index).getLexeme());
        assertEquals(null, tokens.get(index).getLiteral());
    }

    @Test
    void testBasicTokens() {

        String source = "let x = 10;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        // let
        assertEquals(
                TokenType.LET,
                tokens.get(0).getType()
        );

        assertEquals(
                "let",
                tokens.get(0).getLexeme()
        );

        // x
        assertEquals(
                TokenType.IDENTIFIER,
                tokens.get(1).getType()
        );

        assertEquals(
                "x",
                tokens.get(1).getLexeme()
        );

        // =
        assertEquals(
                TokenType.EQUAL,
                tokens.get(2).getType()
        );

        assertEquals(
                "=",
                tokens.get(2).getLexeme()
        );

        // 10
        assertEquals(
                TokenType.NUMBER,
                tokens.get(3).getType()
        );

        assertEquals(
                "10",
                tokens.get(3).getLexeme()
        );

        // ;
        assertEquals(
                TokenType.SEMICOLON,
                tokens.get(4).getType()
        );

        assertEquals(
                ";",
                tokens.get(4).getLexeme()
        );

        // EOF
        assertEquals(
                TokenType.EOF,
                tokens.get(5).getType()
        );
    }

    @Test
    void testNumberLiterals() {
        List<Token> tokens = scan("0; 123; 45.67; 8 9 10;");

        assertToken(tokens, 0, TokenType.NUMBER, "0", 0.0);
        assertToken(tokens, 1, TokenType.SEMICOLON, ";", null);
        assertToken(tokens, 2, TokenType.NUMBER, "123", 123.0);
        assertToken(tokens, 3, TokenType.SEMICOLON, ";", null);
        assertToken(tokens, 4, TokenType.NUMBER, "45.67", 45.67);
        assertToken(tokens, 5, TokenType.SEMICOLON, ";", null);
        assertToken(tokens, 6, TokenType.NUMBER, "8", 8.0);
        assertToken(tokens, 7, TokenType.NUMBER, "9", 9.0);
        assertToken(tokens, 8, TokenType.NUMBER, "10", 10.0);
        assertToken(tokens, 9, TokenType.SEMICOLON, ";", null);
        assertEof(tokens, 10);
    }

    @Test
    void testStringLiterals() {
        List<Token> tokens = scan("\"hello\"; \"hello world\"; \"a\" \"b\";");

        assertToken(tokens, 0, TokenType.STRING, "\"hello\"", "hello");
        assertToken(tokens, 1, TokenType.SEMICOLON, ";", null);
        assertToken(tokens, 2, TokenType.STRING, "\"hello world\"", "hello world");
        assertToken(tokens, 3, TokenType.SEMICOLON, ";", null);
        assertToken(tokens, 4, TokenType.STRING, "\"a\"", "a");
        assertToken(tokens, 5, TokenType.STRING, "\"b\"", "b");
        assertToken(tokens, 6, TokenType.SEMICOLON, ";", null);
        assertEof(tokens, 7);
    }

    @Test
    void testIdentifiers() {
        List<Token> tokens = scan("name camelCase value1 _hidden;");

        assertToken(tokens, 0, TokenType.IDENTIFIER, "name", null);
        assertToken(tokens, 1, TokenType.IDENTIFIER, "camelCase", null);
        assertToken(tokens, 2, TokenType.IDENTIFIER, "value1", null);
        assertToken(tokens, 3, TokenType.IDENTIFIER, "_hidden", null);
        assertToken(tokens, 4, TokenType.SEMICOLON, ";", null);
        assertEof(tokens, 5);
    }

    @Test
    void testKeywords() {
        List<Token> tokens = scan("let if else while fun return and or true false null print;");

        assertToken(tokens, 0, TokenType.LET, "let", null);
        assertToken(tokens, 1, TokenType.IF, "if", null);
        assertToken(tokens, 2, TokenType.ELSE, "else", null);
        assertToken(tokens, 3, TokenType.WHILE, "while", null);
        assertToken(tokens, 4, TokenType.FUN, "fun", null);
        assertToken(tokens, 5, TokenType.RETURN, "return", null);
        assertToken(tokens, 6, TokenType.AND, "and", null);
        assertToken(tokens, 7, TokenType.OR, "or", null);
        assertToken(tokens, 8, TokenType.TRUE, "true", null);
        assertToken(tokens, 9, TokenType.FALSE, "false", null);
        assertToken(tokens, 10, TokenType.NULL, "null", null);
        assertToken(tokens, 11, TokenType.PRINT, "print", null);
        assertToken(tokens, 12, TokenType.SEMICOLON, ";", null);
        assertEof(tokens, 13);
    }

    @Test
    void testOperatorsAndPunctuation() {
        List<Token> tokens = scan("+-*/ = == != < <= > >= ! () {} , . ;");

        assertToken(tokens, 0, TokenType.PLUS, "+", null);
        assertToken(tokens, 1, TokenType.MINUS, "-", null);
        assertToken(tokens, 2, TokenType.STAR, "*", null);
        assertToken(tokens, 3, TokenType.SLASH, "/", null);
        assertToken(tokens, 4, TokenType.EQUAL, "=", null);
        assertToken(tokens, 5, TokenType.EQUAL_EQUAL, "==", null);
        assertToken(tokens, 6, TokenType.BANG_EQUAL, "!=", null);
        assertToken(tokens, 7, TokenType.LESS, "<", null);
        assertToken(tokens, 8, TokenType.LESS_EQUAL, "<=", null);
        assertToken(tokens, 9, TokenType.GREATER, ">", null);
        assertToken(tokens, 10, TokenType.GREATER_EQUAL, ">=", null);
        assertToken(tokens, 11, TokenType.BANG, "!", null);
        assertToken(tokens, 12, TokenType.LEFT_PAREN, "(", null);
        assertToken(tokens, 13, TokenType.RIGHT_PAREN, ")", null);
        assertToken(tokens, 14, TokenType.LEFT_BRACE, "{", null);
        assertToken(tokens, 15, TokenType.RIGHT_BRACE, "}", null);
        assertToken(tokens, 16, TokenType.COMMA, ",", null);
        assertToken(tokens, 17, TokenType.DOT, ".", null);
        assertToken(tokens, 18, TokenType.SEMICOLON, ";", null);
        assertEof(tokens, 19);
    }

    @Test
    void testCommentsAndWhitespace() {
        List<Token> tokens = scan("""
                let x = 1; // first comment
                // whole comment line
                let y = 2; // trailing
                x + y;
                """);

        assertToken(tokens, 0, TokenType.LET, "let", null);
        assertToken(tokens, 1, TokenType.IDENTIFIER, "x", null);
        assertToken(tokens, 2, TokenType.EQUAL, "=", null);
        assertToken(tokens, 3, TokenType.NUMBER, "1", 1.0);
        assertToken(tokens, 4, TokenType.SEMICOLON, ";", null);
        assertToken(tokens, 5, TokenType.LET, "let", null);
        assertToken(tokens, 6, TokenType.IDENTIFIER, "y", null);
        assertToken(tokens, 7, TokenType.EQUAL, "=", null);
        assertToken(tokens, 8, TokenType.NUMBER, "2", 2.0);
        assertToken(tokens, 9, TokenType.SEMICOLON, ";", null);
        assertToken(tokens, 10, TokenType.IDENTIFIER, "x", null);
        assertToken(tokens, 11, TokenType.PLUS, "+", null);
        assertToken(tokens, 12, TokenType.IDENTIFIER, "y", null);
        assertToken(tokens, 13, TokenType.SEMICOLON, ";", null);
        assertEof(tokens, 14);
    }

    @Test
    void testLineAndColumnTracking() {
        List<Token> tokens = scan("""
                let x = 1;
                print x;
                if (x >= 1) {
                    x = x + 1;
                }
                """);

        assertEquals(1, tokens.get(0).getLine());
        assertEquals(1, tokens.get(0).getColumn());
        assertEquals(2, tokens.get(5).getLine());
        assertEquals(1, tokens.get(5).getColumn());
        assertEquals(3, tokens.get(8).getLine());
        assertEquals(1, tokens.get(8).getColumn());
        assertEquals(4, tokens.get(17).getLine());
        assertTrue(tokens.get(17).getColumn() >= 1);
    }

    @Test
    void testRepresentativeProgramTokenization() {
        List<Token> tokens = scan("""
                let x = 10;
                let y = x + 5;
                if (y > 10) {
                    print y;
                }
                """);

        assertToken(tokens, 0, TokenType.LET, "let", null);
        assertToken(tokens, 1, TokenType.IDENTIFIER, "x", null);
        assertToken(tokens, 2, TokenType.EQUAL, "=", null);
        assertToken(tokens, 3, TokenType.NUMBER, "10", 10.0);
        assertToken(tokens, 4, TokenType.SEMICOLON, ";", null);
        assertToken(tokens, 5, TokenType.LET, "let", null);
        assertToken(tokens, 6, TokenType.IDENTIFIER, "y", null);
        assertToken(tokens, 7, TokenType.EQUAL, "=", null);
        assertToken(tokens, 8, TokenType.IDENTIFIER, "x", null);
        assertToken(tokens, 9, TokenType.PLUS, "+", null);
        assertToken(tokens, 10, TokenType.NUMBER, "5", 5.0);
        assertToken(tokens, 11, TokenType.SEMICOLON, ";", null);
        assertToken(tokens, 12, TokenType.IF, "if", null);
        assertToken(tokens, 13, TokenType.LEFT_PAREN, "(", null);
        assertToken(tokens, 14, TokenType.IDENTIFIER, "y", null);
        assertToken(tokens, 15, TokenType.GREATER, ">", null);
        assertToken(tokens, 16, TokenType.NUMBER, "10", 10.0);
        assertToken(tokens, 17, TokenType.RIGHT_PAREN, ")", null);
        assertToken(tokens, 18, TokenType.LEFT_BRACE, "{", null);
        assertToken(tokens, 19, TokenType.PRINT, "print", null);
        assertToken(tokens, 20, TokenType.IDENTIFIER, "y", null);
        assertToken(tokens, 21, TokenType.SEMICOLON, ";", null);
        assertToken(tokens, 22, TokenType.RIGHT_BRACE, "}", null);
        assertEof(tokens, 23);
    }

    @Test
    void testUnexpectedCharacterThrowsLexiLexerException() {

        Lexer lexer = new Lexer("let x = @;");

        LexiLexerException exception = assertThrows(
                LexiLexerException.class,
                lexer::scanTokens
        );

        assertTrue(exception.getMessage().contains("Unexpected character '@'"));
        assertEquals(1, exception.getLine());
        assertEquals(9, exception.getColumn());
    }

    @Test
    void testUnterminatedStringThrowsLexiLexerException() {

        Lexer lexer = new Lexer("\"hello");

        LexiLexerException exception = assertThrows(
                LexiLexerException.class,
                lexer::scanTokens
        );

        assertTrue(exception.getMessage().contains("Unterminated string"));
        assertEquals(1, exception.getLine());
        assertTrue(exception.getColumn() >= 1);
    }
}