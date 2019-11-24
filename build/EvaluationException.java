/**
 * Represents an error encountered during evaluation of a program
 *
 * @since 24/11/19
 * @author Saul Johnson <saul.a.johnson@gmail.com>
 */
@SuppressWarnings({"unused"}) // API class.
public class EvaluationException extends Exception {

    private int line;

    private int column;

    /**
     * Initializes a new instance of an evaluation exception.
     *
     * @param s         the exception message
     * @param line      the line at which the exception was raised
     * @param column    the column at which the exception was raised
     */
    public EvaluationException(String s, int line, int column) {
        super(s + " at line " + line + " column " + column + ".");
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
