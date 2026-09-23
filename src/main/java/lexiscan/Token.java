package lexiscan;

public record Token(
        TokenType type,
        String lexeme,
        Object literal,
        int line,
        int column
) {

    public Token(
            TokenType type,
            String lexeme,
            int line,
            int column
    ) {
        this(type, lexeme, null, line, column);
    }

    // Compatibility getters
    public TokenType getType() {
        return type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public Object getLiteral() {
        return literal;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}