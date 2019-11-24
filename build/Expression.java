/**
 * Represents an abstract expression.
 *
 * @since 23/11/19
 * @author Saul Johnson <saul.a.johnson@gmail.com>
 */
public abstract class Expression {

    protected Token token;

    private ExpressionType type;

    /**
     * Initialises a new instance of an abstract expression.
     *
     * @param type  the type of expression to represent
     */
    protected Expression(ExpressionType type) {
        this.type = type;
    }

    /**
     * Gets the type of expression this instance represents.
     *
     * @return  the expression type
     */
    public ExpressionType getType() {
        return type;
    }

    /**
     * Gets the token the makes up this atom.
     *
     * @return  the token
     */
    public Token getToken() {
        return token;
    }

    /**
     * Reads the tokens comprising an expression from the given token stream and returns the parsed result.
     *
     * @param tokenStream       the token stream to read from
     * @return                  the parsed result
     * @throws ParseException   if parsing fails
     */
    public static Expression parse(TokenStream tokenStream) throws ParseException {

        // Peek expecting one of two possibilities.
        TokenType[] possibilities = new TokenType[]{TokenType.OPEN_PARENTHESIS, TokenType.INTEGER};
        switch (tokenStream.peekExpectingOneOf(possibilities).getType()) {
            case OPEN_PARENTHESIS:
                return Call.parse(tokenStream); // An open parenthesis indicates a call.
            case INTEGER:
                return Atom.parse(tokenStream); // Otherwise we have an atomic value.
            default:
                return null; // Should never be triggered.
        }
    }
}
