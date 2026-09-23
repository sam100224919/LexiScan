package lexiscan.ast;

import lexiscan.Token;

public class VarStmt extends Stmt {

    private final Token name;
    public final Expr initializer;

    public VarStmt(Token name, Expr initializer) {
        this.name = name;
        this.initializer = initializer;
    }

    public Token getName() {
        return name;
    }

    public Expr getInitializer() {
        return initializer;
    }
}
