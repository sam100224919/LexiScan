package lexiscan;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LexerTest {

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
}