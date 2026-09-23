package lexiscan;

import lexiscan.ast.BinaryExpr;
import lexiscan.ast.Expr;
import lexiscan.ast.ExprStmt;
import lexiscan.ast.LiteralExpr;
import lexiscan.ast.Stmt;
import lexiscan.ast.VarStmt;
import lexiscan.ast.VariableExpr;

import java.util.List;

public class Interpreter {
    private final Environment environment = new Environment();

    public Object interpret(List<Stmt> statements) {
        for (Stmt statement : statements) {
            interpret(statement);
        }
        return null;
    }

    public Object interpret(Expr expr) {
        return evaluate(expr);
    }

    public void interpret(Stmt stmt) {
        if (stmt instanceof VarStmt varStmt) {
            Object value = null;
            if (varStmt.getInitializer() != null) {
                value = evaluate(varStmt.getInitializer());
            }
            environment.define(varStmt.getName().lexeme(), value);
        } else if (stmt instanceof ExprStmt exprStmt) {
            evaluate(exprStmt.getExpression());
        } else {
            throw new RuntimeException("Unknown statement type: " + stmt.getClass().getSimpleName());
        }
    }

    public Object evaluate(Expr expr) {
        if (expr instanceof LiteralExpr literal) {
            return literal.getValue();
        }

        if (expr instanceof VariableExpr varExpr) {
            return environment.get(varExpr.getName());
        }

        if (expr instanceof BinaryExpr binary) {
            Object left = evaluate(binary.getLeft());
            Object right = evaluate(binary.getRight());

            if (binary.getOperator().type() == TokenType.PLUS) {
                if (left instanceof Double l && right instanceof Double r) {
                    return l + r;
                }
            }
        }

        throw new RuntimeException("Runtime error: Unknown expression type.");
    }

    public Object execute(List<Stmt> statements) {
        Object result = null;

        for (Stmt statement : statements) {
            if (statement instanceof ExprStmt exprStmt) {
                result = interpret(exprStmt.getExpression());
            } else if (statement instanceof VarStmt varStmt) {
                Object value = interpret(varStmt.getInitializer());
                environment.define(
                        varStmt.getName().lexeme(),
                        value
                );
                result = value;
            }
        }

        return result;
    }
}