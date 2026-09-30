package nynguage.lexer;

public class LexicalError extends RuntimeException {
    public final int line, col;

    public LexicalError(String message, int line, int col) {
        super(message);
        this.line = line;
        this.col = col;
    }
}