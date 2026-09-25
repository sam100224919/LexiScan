package lexiscan.ast;

import lexiscan.Token;

import java.util.List;

public class FunctionStmt extends Stmt {

    private final Token name;
    private final List<Token> params;
    private final List<Stmt> body;

    public FunctionStmt(
            Token name,
            List<Token> params,
            List<Stmt> body
    ) {
        this.name = name;
        this.params = params;
        this.body = body;
    }

    public Token getName() {
        return name;
    }

    public List<Token> getParams() {
        return params;
    }

    public List<Stmt> getBody() {
        return body;
    }
}
