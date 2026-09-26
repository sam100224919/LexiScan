package lexiscan;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LexiScanTest {

    @Test
    void testVariableDeclarationAndArithmetic() {
        LexiScan lexiScan = new LexiScan();

        Object result = lexiScan.run("""
                let x = 10;
                let y = 5;
                x + y;
                """);

        assertEquals(15.0, result);
    }

    @Test
    void testVariableReassignment() {
        LexiScan lexiScan = new LexiScan();

        Object result = lexiScan.run("""
                let x = 10;
                x = 20;
                x;
                """);

        assertEquals(20.0, result);
    }

    @Test
    void testOperatorPrecedence() {
        LexiScan lexiScan = new LexiScan();

        Object result = lexiScan.run("2 + 3 * 4;");

        assertEquals(14.0, result);
    }

    @Test
    void testComparisonAndEquality() {
        LexiScan lexiScan = new LexiScan();

        Object result = lexiScan.run("(10 > 5) == (3 < 1);");

        assertEquals(false, result);
    }

    @Test
    void testBooleanExpressions() {
        LexiScan lexiScan = new LexiScan();

        Object result = lexiScan.run("true and !false;");

        assertEquals(true, result);
    }

    @Test
    void testIfElse() {
        LexiScan lexiScan = new LexiScan();

        Object result = lexiScan.run("""
                let x = 0;
                if (true) {
                    x = 1;
                } else {
                    x = 2;
                }
                x;
                """);

        assertEquals(1.0, result);
    }

    @Test
    void testWhileLoop() {
        LexiScan lexiScan = new LexiScan();

        Object result = lexiScan.run("""
                let x = 0;
                while (x < 3) {
                    x = x + 1;
                }
                x;
                """);

        assertEquals(3.0, result);
    }

    @Test
    void testNestedBlockScope() {
        LexiScan lexiScan = new LexiScan();

        Object result = lexiScan.run("""
                let x = 1;
                {
                    let x = 2;
                }
                x;
                """);

        assertEquals(1.0, result);
    }

    @Test
    void testFunctionDeclarationAndCall() {
        LexiScan lexiScan = new LexiScan();

        Object result = lexiScan.run("""
                fun addOne(value) {
                    return value + 1;
                }
                addOne(10);
                """);

        assertEquals(11.0, result);
    }

    @Test
    void testFunctionParameters() {
        LexiScan lexiScan = new LexiScan();

        Object result = lexiScan.run("""
                fun sum(a, b, c) {
                    return a + b + c;
                }
                sum(2, 3, 4);
                """);

        assertEquals(9.0, result);
    }

    @Test
    void testReturn() {
        LexiScan lexiScan = new LexiScan();

        Object result = lexiScan.run("""
                fun compute(x) {
                    return (x * 2) + 3;
                }
                compute(5);
                """);

        assertEquals(13.0, result);
    }

    @Test
    void testRecursion() {
        LexiScan lexiScan = new LexiScan();

        Object result = lexiScan.run("""
                fun factorial(n) {
                    if (n < 2) {
                        return 1;
                    }

                    return n * factorial(n - 1);
                }
                factorial(5);
                """);

        assertEquals(120.0, result);
    }

    @Test
    void testClosure() {
        LexiScan lexiScan = new LexiScan();

        Object result = lexiScan.run("""
                let x = 100;
                fun readX() {
                    return x;
                }
                {
                    let x = 1;
                    readX();
                }
                """);

        assertEquals(100.0, result);
    }

    @Test
    void testPrint() {
        LexiScan lexiScan = new LexiScan();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        Object result;
        try {
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));

            result = lexiScan.run("""
                    print "Hello";
                    print 10 + 5;
                    let x = 20;
                    print x;
                    """);
        } finally {
            System.setOut(originalOut);
        }

        String expected = "Hello" + System.lineSeparator()
                + "15.0" + System.lineSeparator()
                + "20.0" + System.lineSeparator();

        assertEquals(expected, output.toString(StandardCharsets.UTF_8));
        assertEquals(20.0, result);
    }

    @Test
    void testRuntimeErrorPropagation() {
        LexiScan lexiScan = new LexiScan();

        LexiRuntimeException exception = assertThrows(
                LexiRuntimeException.class,
                () -> lexiScan.run("y;")
        );

        assertEquals("Undefined variable 'y'.", exception.getMessage());
    }

    @Test
    void testParserErrorPropagation() {
        LexiScan lexiScan = new LexiScan();

        LexiParserException exception = assertThrows(
                LexiParserException.class,
                () -> lexiScan.run("let x = ;")
        );

        assertTrue(exception.getMessage().contains("Expected expression."));
    }

    @Test
    void testLexerErrorPropagation() {
        LexiScan lexiScan = new LexiScan();

        LexiLexerException exception = assertThrows(
                LexiLexerException.class,
                () -> lexiScan.run("print \"oops")
        );

        assertTrue(exception.getMessage().contains("Unterminated string"));
    }
}