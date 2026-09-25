package lexiscan.ast;

import lexiscan.Token;

public class AssignExpr extends Expr {

    private final Token name;
    private final Expr value;

    public AssignExpr(Token name, Expr value) {
        this.name = name;
        this.value = value;
    }

    public Token getName() {
        return name;
    }

    public Expr getValue() {
        return value;
    }
}