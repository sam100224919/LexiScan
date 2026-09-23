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

        // 1. Lexing
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        // 2. Parsing
        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        // 3. Interpretation
        Interpreter interpreter = new Interpreter();

        // Execute variable declarations so 'x' and 'y' enter the environment
        interpreter.interpret(statements.get(0)); // let x = 10;
        interpreter.interpret(statements.get(1)); // let y = 5;

        // Evaluate the addition statement's inner expression
        ExprStmt exprStmt = (ExprStmt) statements.get(2);
        Object result = interpreter.evaluate(exprStmt.expression);

        // 4. Verify
        assertEquals(15.0, result);
    }
}