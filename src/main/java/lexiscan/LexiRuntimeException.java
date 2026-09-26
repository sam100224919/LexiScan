package lexiscan;

public class LexiRuntimeException extends RuntimeException {

    private final int line;
    private final int column;

    public LexiRuntimeException(String message) {
        super(message);
        this.line = -1;
        this.column = -1;
    }

    public LexiRuntimeException(String message, int line, int column) {
        super(message);
        this.line = line;
        this.column = column;
    }

    public LexiRuntimeException(String message, Token token) {
        this(message, token.line(), token.column());
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
}