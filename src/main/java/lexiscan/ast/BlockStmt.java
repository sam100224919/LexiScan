package lexiscan.ast;

import java.util.List;

public class BlockStmt extends Stmt {

    private final List<Stmt> statements;

    public BlockStmt(List<Stmt> statements) {
        this.statements = statements;
    }

    public List<Stmt> getStatements() {
        return statements;
    }
}