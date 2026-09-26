package lexiscan;

public class LexiLexerException extends RuntimeException {

    private final int line;
    private final int column;

    public LexiLexerException(String message, int line, int column) {
        super(message);
        this.line = line;
        this.column = column;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
}