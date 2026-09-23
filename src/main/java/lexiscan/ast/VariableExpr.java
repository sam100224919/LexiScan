package lexiscan.ast;

import lexiscan.Token;

public class VariableExpr extends Expr {

    private final Token name;

    public VariableExpr(Token name) {
        this.name = name;
    }

    public String getName() { return name.getLexeme(); }
// or return name.getText(); / return name.lexeme; depending on your Token class
}