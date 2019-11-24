import java.util.LinkedList;
import java.util.List;

/**
 * Represents a function call.
 *
 * @since 23/11/19
 * @author Saul Johnson <saul.a.johnson@gmail.com>
 */
@SuppressWarnings({"unused"}) // API class.
public class Call extends Expression {

    private Expression[] expressions;

    private String identifier;

    /**
     * Initialises a new instance of a function call.
     */
    private Call() {
        super(ExpressionType.CALL);
        expressions = new Expression[] {};
    }

    /**
     * Gets the expressions that make up the function call.
     *
     * @return  the program components
     */
    public Expression[] getExpressions() {
        return expressions;
    }

    /**
     * Gets the function identifier (name).
     *
     * @return  the function identifier (name)
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Reads the tokens comprising a function call from the given token stream and returns the parsed result.
     *
     * @param tokenStream       the token stream to read from
     * @return                  the parsed result
     * @throws ParseException   if parsing fails
     */
    public static Call parse(TokenStream tokenStream) throws ParseException {

        // Build list of program components (statements and annotations).
        List<Expression> expressions = new LinkedList<Expression>();

        // Read opening parenthesis.
        tokenStream.readExpecting(TokenType.OPEN_PARENTHESIS);

        // Read function identifier.
        Token token = tokenStream.readExpecting(TokenType.IDENTIFIER);

        // Read until call end.
        while (tokenStream.peek().getType() != TokenType.CLOSE_PARENTHESIS) {
            expressions.add(Expression.parse(tokenStream)); // Parse each expression in the call.
        }

        // Discard closing parenthesis.
        tokenStream.readExpecting(new TokenType[] {TokenType.CLOSE_PARENTHESIS});

        // Create and return block.
        Call output = new Call();
        output.expressions = expressions.toArray(new Expression[] {});
        output.token = token;
        output.identifier = token.getText();
        return output;
    }
}
