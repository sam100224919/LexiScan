package lexiscan;

import lexiscan.ast.ExprStmt;
import lexiscan.ast.Stmt;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        assertEquals(42.0, result);
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

        assertEquals(20.0, result);
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

        assertEquals(5.0, result);
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

        assertEquals(2.0, result);
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

        assertEquals(9.0, result);
    }

    @Test
    void testBasicWhileLoop() {

        String source = """
                let x = 0;
                while (x < 3) {
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
                    let x = x * 2;
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

        assertEquals(7.0, result);
    }

    @Test
    void testWhileLoopWithBooleanLogicCondition() {

        String source = """
                let x = 0;
                while ((x < 5) and !false) {
                    let x = x + 2;
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
                    let x = x + 1;
                    let y = y + x;
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
}