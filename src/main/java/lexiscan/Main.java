package lexiscan;

import lexiscan.ast.Expr;
import lexiscan.ast.Stmt;
import java.util.List;

public class Main {

    public static void main(String[] args) {
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

        // 3. Interpreting
        Interpreter interpreter = new Interpreter();

        System.out.println();
        System.out.println("=== INTERPRETING ===");

        Object result = interpreter.execute(statements);

        System.out.println("Result: " + result);

        System.out.println();
        System.out.println("=== COMPLETE ===");

        System.out.println();
        System.out.println("=== COMPLETE ===");
    }
}