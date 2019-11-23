/**
 * Represents an error encountered during tokenization of a source file.
 *
 * @since 23/11/19
 * @author Saul Johnson <saul.a.johnson@gmail.com>
 */
@SuppressWarnings({"WeakerAccess", "unused"}) // API class.
public class TokenizationException extends Exception {

    private int line;

    private int column;

    /**
     * Initializes a new instance of a tokenization exception.
     *
     * @param s         the exception message
     * @param line      the line at which the exception was raised
     * @param column    the column at which the exception was raised
     */
    public TokenizationException(String s, int line, int column) {
        super(s);
        this.line = line;
        this.column = column;
    }

    /**
     * Gets the line at which the exception was raised
     *
     * @return  the line at which the exception was raised
     */
    public int getLine() {
        return line;
    }

    /**
     * Gets the column at which the exception was raised
     *
     * @return  the column at which the exception was raised
     */
    public int getColumn() {
        return column;
    }
}
