package lexiscan;

import lexiscan.ast.Stmt;

import java.util.List;

public class LexiScan {

    public Object run(String source) {
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Interpreter interpreter = new Interpreter();
        return interpreter.execute(statements);
    }
}