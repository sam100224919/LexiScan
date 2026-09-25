package lexiscan;

import lexiscan.ast.BlockStmt;
import lexiscan.ast.ExprStmt;
import lexiscan.ast.Stmt;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InterpreterTest {

    @Test
    void testVariableAddition() {

        String source = """
                let x = 10;
                let y = 5;
                x + y;
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        interpreter.execute(statements.get(0));
        interpreter.execute(statements.get(1));

        ExprStmt exprStmt = (ExprStmt) statements.get(2);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(15.0, result);
    }

    @Test
    void testVariableReassignment() {

        String source = """
                let x = 10;
                x = 20;
                x;
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        interpreter.execute(statements.get(0));
        interpreter.execute(statements.get(1));

        ExprStmt exprStmt = (ExprStmt) statements.get(2);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(20.0, result);
    }

    @Test
    void testVariableReassignmentWithExpression() {

        String source = """
                let x = 10;
                x = x + 5;
                x;
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        interpreter.execute(statements.get(0));
        interpreter.execute(statements.get(1));

        ExprStmt exprStmt = (ExprStmt) statements.get(2);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(15.0, result);
    }

    @Test
    void testMultipleVariableReassignments() {

        String source = """
                let x = 1;
                x = 2;
                x = 3;
                x;
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        interpreter.execute(statements.get(0));
        interpreter.execute(statements.get(1));
        interpreter.execute(statements.get(2));

        ExprStmt exprStmt = (ExprStmt) statements.get(3);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(3.0, result);
    }

    @Test
    void testAssignmentWithComparisonAndArithmetic() {

        String source = """
                let x = 5;
                x = x * 2 > 9;
                x;
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        interpreter.execute(statements.get(0));
        interpreter.execute(statements.get(1));

        ExprStmt exprStmt = (ExprStmt) statements.get(2);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(true, result);
    }

    @Test
    void testAssignUndefinedVariableThrowsRuntimeError() {

        Interpreter interpreter = new Interpreter();

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> interpreter.getEnvironment().assign(
                        "x",
                        10.0
                )
        );

        assertEquals("Undefined variable 'x'.", exception.getMessage());
    }

    @Test
    void testSubtraction() {

        String source = "10 - 5;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(5.0, result);
    }

    @Test
    void testMultiplication() {

        String source = "10 * 5;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(50.0, result);
    }

    @Test
    void testDivision() {

        String source = "10 / 5;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(2.0, result);
    }

    @Test
    void testBooleanTrueLiteral() {

        String source = "true;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(true, result);
    }

    @Test
    void testBooleanFalseLiteral() {

        String source = "false;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(false, result);
    }

    @Test
    void testNullLiteral() {

        String source = "null;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(null, result);
    }

    @Test
    void testTrueEqualTrue() {

        String source = "true == true;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(true, result);
    }

    @Test
    void testTrueEqualFalse() {

        String source = "true == false;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(false, result);
    }

    @Test
    void testTrueBangEqualFalse() {

        String source = "true != false;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(true, result);
    }

    @Test
    void testNullEqualNull() {

        String source = "null == null;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(true, result);
    }

    @Test
    void testNullBangEqualNull() {

        String source = "null != null;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(false, result);
    }

    @Test
    void testNullEqualTrue() {

        String source = "null == true;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(false, result);
    }

    @Test
    void testGreaterThanRequestedCase() {

        String source = "10 > 5;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(true, result);
    }

    @Test
    void testGreaterThanOrEqualRequestedCase() {

        String source = "10 >= 10;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(true, result);
    }

    @Test
    void testLessThanRequestedCase() {

        String source = "5 < 10;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(true, result);
    }

    @Test
    void testLessThanOrEqualRequestedCase() {

        String source = "5 <= 5;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(true, result);
    }

    @Test
    void testEqualEqualRequestedCase() {

        String source = "10 == 10;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(true, result);
    }

    @Test
    void testBangEqualRequestedCase() {

        String source = "10 != 5;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(true, result);
    }

    @Test
    void testComparisonPrecedenceWithAddition() {

        String source = "2 + 3 > 4;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(true, result);
    }

    @Test
    void testEqualityPrecedenceWithMultiplication() {

        String source = "2 * 3 == 6;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(true, result);
    }

    @Test
    void testEqualityWithParenthesizedAddition() {

        String source = "(2 + 3) == 5;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(true, result);
    }

    @Test
    void testGreaterThan() {

        String source = "10 > 5;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(true, result);
    }

    @Test
    void testLessThan() {

        String source = "3 < 9;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(true, result);
    }

    @Test
    void testLessThanOrEqualEqualCase() {

        String source = "1 <= 1;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(true, result);
    }

    @Test
    void testLessThanOrEqualTrueCase() {

        String source = "1 <= 2;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(true, result);
    }

    @Test
    void testLessThanOrEqualFalseCase() {

        String source = "2 <= 1;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(false, result);
    }

    @Test
    void testGreaterThanOrEqualEqualCase() {

        String source = "1 >= 1;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(true, result);
    }

    @Test
    void testGreaterThanOrEqualTrueCase() {

        String source = "2 >= 1;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(true, result);
    }

    @Test
    void testGreaterThanOrEqualFalseCase() {

        String source = "1 >= 2;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(false, result);
    }

    @Test
    void testEquality() {

        String source = "10 == 10;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(true, result);
    }

    @Test
    void testInequality() {

        String source = "10 != 5;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(true, result);
    }

    @Test
    void testTrueLiteral() {

        String source = "true;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(true, result);
    }

    @Test
    void testFalseLiteral() {

        String source = "false;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(false, result);
    }

    @Test
    void testLogicalAnd() {

        String source = "true and false;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(false, result);
    }

    @Test
    void testLogicalOr() {

        String source = "false or true;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(true, result);
    }

    @Test
    void testLogicalNot() {

        String source = "!false;";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(true, result);
    }

    @Test
    void testCombinedBooleanExpression() {

        String source = "!(false or false) and (10 > 5);";

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        ExprStmt exprStmt = (ExprStmt) statements.get(0);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(true, result);
    }

    @Test
    void testIfTrueCondition() {

        String source = """
                let x = 0;
                if (true) {
                    let x = 42;
                }
                x;
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        for (int i = 0; i < statements.size() - 1; i++) {
            interpreter.execute(statements.get(i));
        }

        ExprStmt exprStmt = (ExprStmt) statements.get(statements.size() - 1);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(0.0, result);
    }

    @Test
    void testIfFalseCondition() {

        String source = """
                let x = 7;
                if (false) {
                    let x = 99;
                }
                x;
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        for (int i = 0; i < statements.size() - 1; i++) {
            interpreter.execute(statements.get(i));
        }

        ExprStmt exprStmt = (ExprStmt) statements.get(statements.size() - 1);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(7.0, result);
    }

    @Test
    void testIfElseBranches() {

        String source = """
                let x = 1;
                if (false) {
                    let x = 10;
                } else {
                    let x = 20;
                }
                x;
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        for (int i = 0; i < statements.size() - 1; i++) {
            interpreter.execute(statements.get(i));
        }

        ExprStmt exprStmt = (ExprStmt) statements.get(statements.size() - 1);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(1.0, result);
    }

    @Test
    void testNestedIfStatements() {

        String source = """
                let x = 0;
                if (true) {
                    if (true) {
                        let x = 5;
                    } else {
                        let x = 6;
                    }
                }
                x;
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        for (int i = 0; i < statements.size() - 1; i++) {
            interpreter.execute(statements.get(i));
        }

        ExprStmt exprStmt = (ExprStmt) statements.get(statements.size() - 1);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(0.0, result);
    }

    @Test
    void testIfConditionWithComparison() {

        String source = """
                let x = 1;
                if (10 > 5) {
                    let x = 2;
                } else {
                    let x = 3;
                }
                x;
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        for (int i = 0; i < statements.size() - 1; i++) {
            interpreter.execute(statements.get(i));
        }

        ExprStmt exprStmt = (ExprStmt) statements.get(statements.size() - 1);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(1.0, result);
    }

    @Test
    void testIfConditionWithBooleanOperators() {

        String source = """
                let x = 0;
                if ((10 > 5) and !false) {
                    let x = 9;
                } else {
                    let x = 4;
                }
                x;
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        for (int i = 0; i < statements.size() - 1; i++) {
            interpreter.execute(statements.get(i));
        }

        ExprStmt exprStmt = (ExprStmt) statements.get(statements.size() - 1);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(0.0, result);

    }

    @Test
    void testIfStatementBasicReassignment() {

        String source = """
                let x = 10;
                if (x > 5) {
                    x = 20;
                }
                x;
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        for (int i = 0; i < statements.size() - 1; i++) {
            interpreter.execute(statements.get(i));
        }

        ExprStmt exprStmt = (ExprStmt) statements.get(statements.size() - 1);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(20.0, result);
    }

    @Test
    void testIfStatementElseBranchReassignment() {

        String source = """
                let x = 3;
                if (x > 5) {
                    x = 20;
                } else {
                    x = 30;
                }
                x;
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        for (int i = 0; i < statements.size() - 1; i++) {
            interpreter.execute(statements.get(i));
        }

        ExprStmt exprStmt = (ExprStmt) statements.get(statements.size() - 1);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(30.0, result);
    }

    @Test
    void testIfStatementFalseWithoutElse() {

        String source = """
                let x = 10;
                if (x < 5) {
                    x = 20;
                }
                x;
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        for (int i = 0; i < statements.size() - 1; i++) {
            interpreter.execute(statements.get(i));
        }

        ExprStmt exprStmt = (ExprStmt) statements.get(statements.size() - 1);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(10.0, result);
    }

    @Test
    void testBlockScopeShadowsOuterVariable() {

        String source = """
                let x = 10;
                {
                    let x = 20;
                }
                x;
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        for (int i = 0; i < statements.size() - 1; i++) {
            interpreter.execute(statements.get(i));
        }

        ExprStmt exprStmt = (ExprStmt) statements.get(statements.size() - 1);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(10.0, result);
    }

    @Test
    void testNestedBlocksPreserveOuterVariable() {

        String source = """
                let x = 10;
                {
                    let y = 20;
                    {
                        let z = 30;
                    }
                }
                x;
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        for (int i = 0; i < statements.size() - 1; i++) {
            interpreter.execute(statements.get(i));
        }

        ExprStmt exprStmt = (ExprStmt) statements.get(statements.size() - 1);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(10.0, result);
    }

    @Test
    void testOuterVariableAccessAndAssignmentFromBlock() {

        String source = """
                let x = 10;
                {
                    x = 20;
                }
                x;
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        for (int i = 0; i < statements.size() - 1; i++) {
            interpreter.execute(statements.get(i));
        }

        ExprStmt exprStmt = (ExprStmt) statements.get(statements.size() - 1);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(20.0, result);
    }

    @Test
    void testUndefinedVariableInsideBlockThrowsRuntimeError() {

        String source = """
                {
                    y;
                }
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> interpreter.execute(statements.get(0))
        );

        assertEquals("Undefined variable 'y'.", exception.getMessage());
    }

    @Test
    void testIfConditionMustBeBoolean() {

        String source = """
                if (123) {
                    let x = 1;
                }
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> interpreter.execute(statements.get(0))
        );

        assertEquals("If condition must be boolean.", exception.getMessage());
    }

    @Test
    void testNestedIfElseControlFlow() {

        String source = """
                let x = 0;
                if (true) {
                    if (false) {
                        x = 1;
                    } else {
                        x = 2;
                    }
                } else {
                    x = 3;
                }
                x;
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        for (int i = 0; i < statements.size() - 1; i++) {
            interpreter.execute(statements.get(i));
        }

        ExprStmt exprStmt = (ExprStmt) statements.get(statements.size() - 1);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(2.0, result);
    }

    @Test
    void testBasicWhileLoop() {

        String source = """
                let x = 0;
                while (x < 3) {
                    x = x + 1;
                }
                x;
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        for (int i = 0; i < statements.size() - 1; i++) {
            interpreter.execute(statements.get(i));
        }

        ExprStmt exprStmt = (ExprStmt) statements.get(statements.size() - 1);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(3.0, result);
    }

    @Test
    void testWhileLoopZeroExecutions() {

        String source = """
                let x = 10;
                while (false) {
                    let x = x + 1;
                }
                x;
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        for (int i = 0; i < statements.size() - 1; i++) {
            interpreter.execute(statements.get(i));
        }

        ExprStmt exprStmt = (ExprStmt) statements.get(statements.size() - 1);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(10.0, result);
    }

    @Test
    void testWhileLoopMultipleExecutions() {

        String source = """
                let x = 1;
                while (x < 16) {
                    x = x * 2;
                }
                x;
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        for (int i = 0; i < statements.size() - 1; i++) {
            interpreter.execute(statements.get(i));
        }

        ExprStmt exprStmt = (ExprStmt) statements.get(statements.size() - 1);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(16.0, result);
    }

    @Test
    void testWhileLoopWithComparisonCondition() {

        String source = """
                let x = 2;
                while (x != 7) {
                    x = x + 1;
                }
                x;
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        for (int i = 0; i < statements.size() - 1; i++) {
            interpreter.execute(statements.get(i));
        }

        ExprStmt exprStmt = (ExprStmt) statements.get(statements.size() - 1);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(7.0, result);
    }

    @Test
    void testWhileLoopWithBooleanLogicCondition() {

        String source = """
                let x = 0;
                while ((x < 5) and !false) {
                    x = x + 2;
                }
                x;
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        for (int i = 0; i < statements.size() - 1; i++) {
            interpreter.execute(statements.get(i));
        }

        ExprStmt exprStmt = (ExprStmt) statements.get(statements.size() - 1);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(6.0, result);
    }

    @Test
    void testWhileLoopWithMultipleStatementsInBody() {

        String source = """
                let x = 0;
                let y = 0;
                while (x < 3) {
                    x = x + 1;
                    y = y + x;
                }
                y;
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        for (int i = 0; i < statements.size() - 1; i++) {
            interpreter.execute(statements.get(i));
        }

        ExprStmt exprStmt = (ExprStmt) statements.get(statements.size() - 1);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(6.0, result);
    }

    @Test
    void testFunctionDeclarationAndCall() {

        String source = """
                fun addOne(value) {
                    return value + 1;
                }
                addOne(10);
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        interpreter.execute(statements.get(0));

        ExprStmt exprStmt = (ExprStmt) statements.get(1);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(11.0, result);
    }

    @Test
    void testFunctionWithMultipleParameters() {

        String source = """
                fun sum(a, b, c) {
                    return a + b + c;
                }
                sum(2, 3, 4);
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        interpreter.execute(statements.get(0));

        ExprStmt exprStmt = (ExprStmt) statements.get(1);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(9.0, result);
    }

    @Test
    void testFunctionReturnExpression() {

        String source = """
                fun compute(x) {
                    return (x * 2) + 3;
                }
                compute(5);
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        interpreter.execute(statements.get(0));

        ExprStmt exprStmt = (ExprStmt) statements.get(1);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(13.0, result);
    }

    @Test
    void testFunctionUsesDeclarationScope() {

        String source = """
                let x = 100;
                fun readX() {
                    return x;
                }
                {
                    let x = 1;
                    readX();
                }
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        interpreter.execute(statements.get(0));
        interpreter.execute(statements.get(1));

        BlockStmt blockStmt = (BlockStmt) statements.get(2);
        ExprStmt exprStmt = (ExprStmt) blockStmt.getStatements().get(1);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(100.0, result);
    }

    @Test
    void testRecursiveFunctionCall() {

        String source = """
                fun factorial(n) {
                    if (n < 2) {
                        return 1;
                    }

                    return n * factorial(n - 1);
                }
                factorial(5);
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();

        interpreter.execute(statements.get(0));

        ExprStmt exprStmt = (ExprStmt) statements.get(1);

        Object result = interpreter.interpret(
                exprStmt.getExpression()
        );

        assertEquals(120.0, result);
    }
}