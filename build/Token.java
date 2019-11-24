/**
 * Represents a source code token.
 *
 * @since 23/11/19
 * @author Saul Johnson <saul.a.johnson@gmail.com>
 */
public class Token {

    private String text;

    private TokenType type;

    private int line;

    private int column;

    /**
     * Initializes a new instance of a token.
     *
     * @param text      the text comprising the token
     * @param type      the type of token this is
     * @param line      the line number at which this token occurs
     * @param column    the column number at which this token occurs
     */
    public Token(String text, TokenType type, int line, int column) {
        this.text = text;
        this.type = type;
        this.line = line;
        this.column = column;
    }

    /**
     * Gets the text comprising this token.
     *
     * @return  the text comprising this token
     */
    public String getText() {
        return text;
    }

    /**
     * Gets the type of token this is
     *
     * @return  the type of token this is
     */
    public TokenType getType() {
        return type;
    }

    /**
     * Gets the line number at which this token occurs.
     *
     * @return  the line number
     */
    public int getLine() {
        return line;
    }

    /**
     * Gets the column number at which this token occurs.
     *
     * @return  the column number
     */
    public int getColumn() {
        return column;
    }
}
