package lexiscan.ast;

public class ExprStmt extends Stmt {

    public final Expr expression;

    public ExprStmt(Expr expression) {
        this.expression = expression;
    }

    public Expr getExpression() {
        return expression;
    }
}