package lexiscan.ast;

import lexiscan.Token;

import java.util.List;

public class CallExpr extends Expr {

    private final Expr callee;
    private final Token paren;
    private final List<Expr> arguments;

    public CallExpr(
            Expr callee,
            Token paren,
            List<Expr> arguments
    ) {
        this.callee = callee;
        this.paren = paren;
        this.arguments = arguments;
    }

    public Expr getCallee() {
        return callee;
    }

    public Token getParen() {
        return paren;
    }

    public List<Expr> getArguments() {
        return arguments;
    }
}
