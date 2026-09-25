package lexiscan.ast;

import lexiscan.Token;

public class UnaryExpr extends Expr {

    private final Token operator;
    private final Expr right;

    public UnaryExpr(Token operator, Expr right) {
        this.operator = operator;
        this.right = right;
    }

    public Token getOperator() {
        return operator;
    }

    public Expr getRight() {
        return right;
    }
}