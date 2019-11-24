/**
 * Represents an atomic value.
 *
 * @since 23/11/19
 * @author Saul Johnson <saul.a.johnson@gmail.com>
 */
@SuppressWarnings({"unused"}) // API class.
public class Atom extends Expression {

    private int value;

    /**
     * Initialises a new instance of an atomic value.
     */
    public Atom() {
        super(ExpressionType.ATOM);
    }

    /**
     * Gets the value this token represents.
     *
     * @return  the token
     */
    public int getValue() {
        return value;
    }

    /**
     * Reads the tokens comprising an atom from the given token stream and returns the parsed result.
     *
     * @param tokenStream       the token stream to read from
     * @return                  the parsed result
     * @throws ParseException   if parsing fails
     */
    public static Atom parse(TokenStream tokenStream) throws ParseException {

        // Read opening parenthesis.
        Token token = tokenStream.readExpecting(TokenType.INTEGER);

        // Create and return block.
        Atom output = new Atom();
        output.token = token;
        output.value = Integer.parseInt(token.getText());
        return output;
    }
}
