package lexiscan.ast;

import lexiscan.Token;

public class ReturnStmt extends Stmt {

    private final Token keyword;
    private final Expr value;

    public ReturnStmt(Token keyword, Expr value) {
        this.keyword = keyword;
        this.value = value;
    }

    public Token getKeyword() {
        return keyword;
    }

    public Expr getValue() {
        return value;
    }
}
