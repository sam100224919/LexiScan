package lexiscan;

public class LexiParserException extends RuntimeException {

    private final Token token;
    private final int line;
    private final int column;

    public LexiParserException(String message, Token token) {
        super(message);
        this.token = token;
        this.line = token.line();
        this.column = token.column();
    }

    public Token getToken() {
        return token;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
}