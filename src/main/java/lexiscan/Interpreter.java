package lexiscan;

import lexiscan.ast.BinaryExpr;
import lexiscan.ast.BlockStmt;
import lexiscan.ast.Expr;
import lexiscan.ast.ExprStmt;
import lexiscan.ast.IfStmt;
import lexiscan.ast.LiteralExpr;
import lexiscan.ast.Stmt;
import lexiscan.ast.UnaryExpr;
import lexiscan.ast.VarStmt;
import lexiscan.ast.VariableExpr;
import lexiscan.ast.WhileStmt;

import java.util.List;

public class Interpreter {

    private final Environment environment;

    public Interpreter() {
        environment = new Environment();
    }

    // =====================================================
    // INTERPRET EXPRESSION
    // =====================================================

    public Object interpret(Expr expression) {
        try {
            return evaluate(expression);
        } catch (RuntimeException e) {
            System.err.println("Runtime error: " + e.getMessage());
            return null;
        }
    }

    // =====================================================
    // EXECUTE ONE STATEMENT
    // =====================================================

    public Object execute(Stmt statement) {

        // Expression statement
        if (statement instanceof ExprStmt exprStmt) {
            return interpret(exprStmt.getExpression());
        }

        // Variable declaration
        if (statement instanceof VarStmt varStmt) {

            Object value = interpret(
                    varStmt.getInitializer()
            );

            environment.define(
                    varStmt.getName().lexeme(),
                    value
            );

            return value;
        }

        // Block statement
        if (statement instanceof BlockStmt blockStmt) {
            return execute(blockStmt.getStatements());
        }

        // If statement
        if (statement instanceof IfStmt ifStmt) {

            Object condition = interpret(
                    ifStmt.getCondition()
            );

            if (isTruthy(condition)) {
                return execute(ifStmt.getThenBranch());
            }

            if (ifStmt.getElseBranch() != null) {
                return execute(ifStmt.getElseBranch());
            }

            return null;
        }

        // While statement
        if (statement instanceof WhileStmt whileStmt) {

            Object result = null;

            while (isTruthy(interpret(whileStmt.getCondition()))) {
                result = execute(whileStmt.getBody());
            }

            return result;
        }

        throw new RuntimeException(
                "Unknown statement."
        );
    }

    // =====================================================
    // EXECUTE LIST OF STATEMENTS
    // =====================================================

    public Object execute(List<Stmt> statements) {

        Object result = null;

        for (Stmt statement : statements) {
            result = execute(statement);
        }

        return result;
    }

    // =====================================================
    // EVALUATE EXPRESSION
    // =====================================================

    private Object evaluate(Expr expression) {

        // =================================================
        // LITERAL
        // =================================================

        if (expression instanceof LiteralExpr literal) {
            return literal.getValue();
        }

        // =================================================
        // VARIABLE
        // =================================================

        if (expression instanceof VariableExpr variable) {
            return environment.get(
                    variable.getName()
            );
        }

        // =================================================
        // UNARY EXPRESSION
        // =================================================

        if (expression instanceof UnaryExpr unary) {

            Object right = evaluate(
                    unary.getRight()
            );

            Token operator = unary.getOperator();

            switch (operator.type()) {

                case BANG:
                    return !toBoolean(
                            operator,
                            right
                    );

                default:
                    throw new RuntimeException(
                            "Unknown unary operator: "
                                    + operator.lexeme()
                    );
            }
        }

        // =================================================
        // BINARY EXPRESSION
        // =================================================

        if (expression instanceof BinaryExpr binary) {

            Object left = evaluate(
                    binary.getLeft()
            );

            Object right = evaluate(
                    binary.getRight()
            );

            Token operator = binary.getOperator();

            switch (operator.type()) {

                case PLUS:

                    if (left instanceof Number &&
                            right instanceof Number) {

                        return ((Number) left).doubleValue()
                                + ((Number) right).doubleValue();
                    }

                    if (left instanceof String ||
                            right instanceof String) {

                        return String.valueOf(left)
                                + String.valueOf(right);
                    }

                    throw new RuntimeException(
                            "Operands must be numbers or strings."
                    );

                case MINUS:

                    checkNumbers(
                            operator,
                            left,
                            right
                    );

                    return ((Number) left).doubleValue()
                            - ((Number) right).doubleValue();

                case STAR:

                    checkNumbers(
                            operator,
                            left,
                            right
                    );

                    return ((Number) left).doubleValue()
                            * ((Number) right).doubleValue();

                case SLASH:

                    checkNumbers(
                            operator,
                            left,
                            right
                    );

                    double divisor =
                            ((Number) right).doubleValue();

                    if (divisor == 0) {
                        throw new RuntimeException(
                                "Cannot divide by zero."
                        );
                    }

                    return ((Number) left).doubleValue()
                            / divisor;

                case GREATER:

                    checkNumbers(
                            operator,
                            left,
                            right
                    );

                    return ((Number) left).doubleValue()
                            > ((Number) right).doubleValue();

                case GREATER_EQUAL:

                    checkNumbers(
                            operator,
                            left,
                            right
                    );

                    return ((Number) left).doubleValue()
                            >= ((Number) right).doubleValue();

                case LESS:

                    checkNumbers(
                            operator,
                            left,
                            right
                    );

                    return ((Number) left).doubleValue()
                            < ((Number) right).doubleValue();

                case LESS_EQUAL:

                    checkNumbers(
                            operator,
                            left,
                            right
                    );

                    return ((Number) left).doubleValue()
                            <= ((Number) right).doubleValue();

                case EQUAL_EQUAL:

                    return isEqual(
                            left,
                            right
                    );

                case BANG_EQUAL:

                    return !isEqual(
                            left,
                            right
                    );

                case AND:

                    return toBoolean(
                            operator,
                            left
                    ) && toBoolean(
                            operator,
                            right
                    );

                case OR:

                    return toBoolean(
                            operator,
                            left
                    ) || toBoolean(
                            operator,
                            right
                    );

                default:

                    throw new RuntimeException(
                            "Unknown binary operator: "
                                    + operator.lexeme()
                    );
            }
        }

        throw new RuntimeException(
                "Unknown expression type."
        );
    }

    // =====================================================
    // NUMBER CHECK
    // =====================================================

    private void checkNumbers(
            Token operator,
            Object left,
            Object right
    ) {

        if (!(left instanceof Number) ||
                !(right instanceof Number)) {

            throw new RuntimeException(
                    "Operands must be numbers for '"
                            + operator.lexeme()
                            + "'."
            );
        }
    }

    // =====================================================
    // EQUALITY
    // =====================================================

    private boolean isEqual(
            Object left,
            Object right
    ) {

        if (left == null && right == null) {
            return true;
        }

        if (left == null) {
            return false;
        }

        return left.equals(right);
    }

    private boolean toBoolean(
            Token operator,
            Object value
    ) {

        if (value instanceof Boolean booleanValue) {
            return booleanValue;
        }

        throw new RuntimeException(
                "Operand must be boolean for '"
                        + operator.lexeme()
                        + "'."
        );
    }

    private boolean isTruthy(Object value) {

        if (value instanceof Boolean booleanValue) {
            return booleanValue;
        }

        return value != null;
    }
}