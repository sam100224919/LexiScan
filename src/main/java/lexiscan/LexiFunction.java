package lexiscan;

import lexiscan.ast.FunctionStmt;

import java.util.List;

public class LexiFunction implements LexiCallable {

    private final FunctionStmt declaration;
    private final Environment closure;

    public LexiFunction(
            FunctionStmt declaration,
            Environment closure
    ) {
        this.declaration = declaration;
        this.closure = closure;
    }

    @Override
    public int arity() {
        return declaration.getParams().size();
    }

    @Override
    public Object call(
            Interpreter interpreter,
            List<Object> arguments
    ) {
        Environment environment = new Environment(
                closure
        );

        for (int i = 0; i < declaration.getParams().size(); i++) {
            environment.define(
                    declaration.getParams().get(i).lexeme(),
                    arguments.get(i)
            );
        }

        try {
            interpreter.executeBlock(
                    declaration.getBody(),
                    environment
            );
        } catch (ReturnSignal returnSignal) {
            return returnSignal.getValue();
        }

        return null;
    }

    @Override
    public String toString() {
        return "<fn " + declaration.getName().lexeme() + ">";
    }
}
