package lexiscan;

import java.util.List;

public interface LexiCallable {
    int arity();
    Object call(Interpreter interpreter, List<Object> arguments);
}
