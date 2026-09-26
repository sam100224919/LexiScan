package lexiscan;

import lexiscan.ast.BinaryExpr;
import lexiscan.ast.AssignExpr;
import lexiscan.ast.BlockStmt;
import lexiscan.ast.CallExpr;
import lexiscan.ast.Expr;
import lexiscan.ast.ExprStmt;
import lexiscan.ast.FunctionStmt;
import lexiscan.ast.IfStmt;
import lexiscan.ast.LiteralExpr;
import lexiscan.ast.PrintStmt;
import lexiscan.ast.ReturnStmt;
import lexiscan.ast.Stmt;
import lexiscan.ast.UnaryExpr;
import lexiscan.ast.VarStmt;
import lexiscan.ast.VariableExpr;
import lexiscan.ast.WhileStmt;

import java.util.ArrayList;
import java.util.List;

public class Interpreter {

    private Environment environment;

    public Interpreter() {
        environment = new Environment();
    }

    // =====================================================
    // INTERPRET EXPRESSION
    // =====================================================

    public Object interpret(Expr expression) {
        return evaluate(expression);
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

        // Function declaration
        if (statement instanceof FunctionStmt functionStmt) {
            LexiFunction function = new LexiFunction(
                    functionStmt,
                    environment
            );

            environment.define(
                    functionStmt.getName().lexeme(),
                    function
            );

            return function;
        }

        // Return statement
        if (statement instanceof ReturnStmt returnStmt) {
            Object value = null;

            if (returnStmt.getValue() != null) {
                value = interpret(returnStmt.getValue());
            }

            throw new ReturnSignal(value);
        }

        // Print statement
        if (statement instanceof PrintStmt printStmt) {
            Object value = evaluate(
                    printStmt.getExpression()
            );

            System.out.println(value);

            return value;
        }

        // Block statement
        if (statement instanceof BlockStmt blockStmt) {
            return executeBlock(
                    blockStmt.getStatements(),
                    new Environment(environment)
            );
        }

        // If statement
        if (statement instanceof IfStmt ifStmt) {

            Object condition = evaluate(
                    ifStmt.getCondition()
            );

            if (!(condition instanceof Boolean booleanCondition)) {
                throw new LexiRuntimeException(
                        "If condition must be boolean."
                );
            }

            if (booleanCondition) {
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

            while (true) {
                Object condition = evaluate(
                        whileStmt.getCondition()
                );

                if (!(condition instanceof Boolean booleanCondition)) {
                    throw new LexiRuntimeException(
                            "While condition must be boolean."
                    );
                }

                if (!booleanCondition) {
                    break;
                }

                result = execute(whileStmt.getBody());
            }

            return result;
        }

        throw new LexiRuntimeException(
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

    public Object run(String source) {
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        return execute(statements);
    }

    public Object executeBlock(
            List<Stmt> statements,
            Environment blockEnvironment
    ) {
        Environment previous = environment;

        try {
            environment = blockEnvironment;

            Object result = null;

            for (Stmt statement : statements) {
                result = execute(statement);
            }

            return result;
        } finally {
            environment = previous;
        }
    }

    public Environment getEnvironment() {
        return environment;
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
        // ASSIGNMENT
        // =================================================

        if (expression instanceof AssignExpr assign) {

            Object value = evaluate(
                    assign.getValue()
            );

            environment.assign(
                    assign.getName().getLexeme(),
                    value
            );

            return value;
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
        // FUNCTION CALL
        // =================================================

        if (expression instanceof CallExpr callExpr) {
            Object callee = evaluate(callExpr.getCallee());

            if (!(callee instanceof LexiCallable function)) {
                throw new LexiRuntimeException(
                        "Can only call functions."
                );
            }

            List<Object> arguments = new ArrayList<>();

            for (Expr argument : callExpr.getArguments()) {
                arguments.add(evaluate(argument));
            }

            if (arguments.size() != function.arity()) {
                throw new LexiRuntimeException(
                        "Expected " + function.arity()
                                + " arguments but got "
                                + arguments.size() + "."
                );
            }

            return function.call(this, arguments);
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
                    throw new LexiRuntimeException(
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

                    throw new LexiRuntimeException(
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
                        throw new LexiRuntimeException(
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

                    throw new LexiRuntimeException(
                            "Unknown binary operator: "
                                    + operator.lexeme()
                    );
            }
        }

        throw new LexiRuntimeException(
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

            throw new LexiRuntimeException(
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

        throw new LexiRuntimeException(
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