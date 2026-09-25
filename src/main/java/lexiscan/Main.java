package lexiscan;

import lexiscan.ast.Stmt;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        String source = """
                let x = 10;
                let y = 5;
                x + y;
                """;

        System.out.println("=== LEXING ===");

        // 1. Lexing
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        for (Token token : tokens) {
            System.out.println(token);
        }

        System.out.println();
        System.out.println("=== PARSING ===");

        // 2. Parsing
        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        System.out.println("Statements parsed: " + statements.size());

        System.out.println();
        System.out.println("=== INTERPRETING ===");

        // 3. Interpretation
        Interpreter interpreter = new Interpreter();

        Object result = interpreter.execute(statements);

        // 4. Result
        System.out.println("Result: " + result);

        System.out.println();
        System.out.println("=== COMPLETE ===");
    }
}