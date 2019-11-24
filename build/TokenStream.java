import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a stream of tokens.
 *
 * @since 23/11/19
 * @author Saul Johnson <saul.a.johnson@gmail.com>
 */
@SuppressWarnings({"WeakerAccess", "unused"}) // API class.
public class TokenStream {

    private Token[] tokens;

    private int position;

    /**
     * Initialises a new token stream.
     *
     * @param tokens    the tokens to load into the stream
     */
    public TokenStream(Token[] tokens) {
        this.tokens = tokens;
        position = 0;
    }

    /**
     * Returns the next token from the token stream or null if the end has been reached.
     *
     * @return  the next token from the token stream
     */
    public Token tryPeek() {
        return position >= tokens.length ? null : tokens[position];
    }

    /**
     * Returns the next token from the token stream or throws an exception if the end has been reached.
     *
     * @return                  the next token from the token stream
     * @throws ParseException   if the end of the stream has been reached
     */
    public Token peek() throws ParseException {

        // Attempt to peek next token.
        Token buffer = tryPeek();

        // Throw an exception if we're at the end of the stream.
        if (buffer == null) {

            // Get line and column number if we can.
            int line = 0;
            int col = 0;
            if (tokens.length > 0){
                line = tokens[tokens.length - 1].getLine();
                col = tokens[tokens.length - 1].getColumn();
            }

            // Throw exception up the stack.
            throw new ParseException("Parse error, unexpected end of token stream", line, col);
        }
        return buffer;
    }

    /**
     * Returns the next token from the token stream or throws an exception if the end has been reached or the token has
     * an unexpected type.
     *
     * @param types             the types of token to expect
     * @return                  the next token from the token stream
     * @throws ParseException   if the end of the stream has been reached or an unexpected token type appears
     */
    public Token peekExpectingOneOf(TokenType[] types) throws ParseException {

        // Read next token.
        Token buffer = peek();

        // Build allowed token type list.
        List<TokenType> allowedTokenTypes = new LinkedList<TokenType>();
        Collections.addAll(allowedTokenTypes, types);

        // Build list of allowed token type names.
        StringBuilder allowedTypes = new StringBuilder();
        for (TokenType allowedTokenType : allowedTokenTypes) {
            if (allowedTypes.length() > 0) {
                allowedTypes.append(", ");
            }
            allowedTypes.append(allowedTokenType.name());
        }

        // Throw an exception if the token type is unexpected.
        if (!allowedTokenTypes.contains(buffer.getType())) {
            throw new ParseException("Parse error, expected token of type in {" + allowedTypes.toString() + "} but got "
                    + buffer.getType().name() + " instead", buffer.getLine(), buffer.getColumn());
        }
        return buffer;
    }

    /**
     * Returns the next token from the token stream or throws an exception if the end has been reached or the token has
     * an unexpected type.
     *
     * @param type              the type of token to expect
     * @return                  the next token from the token stream
     * @throws ParseException   if the end of the stream has been reached or an unexpected token type appears
     */
    public Token peekExpecting(TokenType type) throws ParseException {
        return readExpectingOneOf(new TokenType[] {type});
    }

    /**
     * Reads the next token from the token stream and returns it or null if the end has been reached.
     *
     * @return  the next token from the token stream
     */
    public Token tryRead() {
        return position >= tokens.length ? null : tokens[position++];
    }

    /**
     * Reads the next token from the token stream and returns it or throws an exception if the end has been reached.
     *
     * @return                  the next token from the token stream
     * @throws ParseException   if the end of the stream has been reached
     */
    public Token read() throws ParseException {

        // Attempt to peek next token and increment position.
        Token buffer = peek();
        position++;

        // Return token in buffer.
        return buffer;
    }

    /**
     * Reads the next token from the token stream and returns it or throws an exception if the end has been reached or
     * the token has an unexpected type.
     *
     * @param types             the type of token to expect
     * @return                  the next token from the token stream
     * @throws ParseException   if the end of the stream has been reached or an unexpected token type appears
     */
    public Token readExpectingOneOf(TokenType[] types) throws ParseException {

        // Attempt to peek next token and increment position.
        Token buffer = peekExpectingOneOf(types);
        position++;

        // Return token in buffer.
        return buffer;
    }

    /**
     * Reads the next token from the token stream and returns it or throws an exception if the end has been reached or
     * the token has an unexpected type.
     *
     * @param type              the type of token to expect
     * @return                  the next token from the token stream
     * @throws ParseException   if the end of the stream has been reached or an unexpected token type appears
     */
    public Token readExpecting(TokenType type) throws ParseException {
        return readExpectingOneOf(new TokenType[] {type});
    }

    /**
     * Reads tokens from the token stream and returns them or throws an exception if the end has been reached or any of
     * the tokens have unexpected types.
     *
     * @param types             the types of token to expect
     * @return                  the next tokens from the token stream
     * @throws ParseException   if the end of the stream has been reached or an unexpected token type appears
     */
    public Token[] readExpecting(TokenType[] types) throws ParseException {
        List<Token> tokensList = new LinkedList<Token>();
        for(TokenType type : types) {
            tokensList.add(readExpecting(type));
        }
        return tokensList.toArray(new Token[] {});
    }

    /**
     * Reads from the stream until a token with a type in a list of token types is encountered, including the stop
     * token.
     *
     * @param types             the token types to stop at
     * @return                  the list of tokens read
     * @throws ParseException   if the end of the stream is reached
     */
    public Token[] readPastAnyOf(TokenType[] types) throws ParseException {

        // We're going to return an array of tokens, build it here.
        List<Token> tokens = new LinkedList<Token>();

        // Build stop token type list.
        List<TokenType> stopTokenTypes = new LinkedList<TokenType>();
        Collections.addAll(stopTokenTypes, types);

        // Read until we hit a token with a type in the list, including the stop token.
        Token buffer;
        do {
            buffer = read();
            tokens.add(buffer);
        } while (!stopTokenTypes.contains(buffer.getType()));

        return tokens.toArray(new Token[] {});
    }

    /**
     * Reads from the stream until a token with a given type is encountered, including the stop token.
     *
     * @param type              the token type to stop at
     * @return                  the list of tokens read
     * @throws ParseException   if the end of the stream is reached
     */
    public Token[] readPast(TokenType type) throws ParseException {
        return readPastAnyOf(new TokenType[] {type});
    }

    /**
     * Reads from the stream until a token with a type in a list of token types is encountered, excluding the stop
     * token.
     *
     * @param types             the token types to stop at
     * @return                  the list of tokens read
     * @throws ParseException   if the end of the stream is reached
     */
    public Token[] readUpToAnyOf(TokenType[] types) throws ParseException {

        // We're going to return an array of tokens, build it here.
        List<Token> tokens = new LinkedList<Token>();

        // Build stop token type list.
        List<TokenType> stopTokenTypes = new LinkedList<TokenType>();
        Collections.addAll(stopTokenTypes, types);

        // Read until we hit a token with a type in the list, excluding the stop token.
        Token buffer = peek();
        while (!stopTokenTypes.contains(buffer.getType())) {
            tokens.add(buffer);
            read();
            buffer = peek();
        }

        return tokens.toArray(new Token[] {});
    }

    /**
     * Reads from the stream until a token with a given type is encountered, excluding the stop token.
     *
     * @param type              the token type to stop at
     * @return                  the list of tokens read
     * @throws ParseException   if the end of the stream is reached
     */
    public Token[] readUpTo(TokenType type) throws ParseException {
        return readUpToAnyOf(new TokenType[] {type});
    }

    /**
     * Returns true if the end of the stream has been reached.
     *
     * @return  true if the end of the stream has been reached, otherwise false
     */
    public boolean isTerminal(){
        return tryPeek() == null;
    }

    /**
     * Discards all leading tokens of a specific type.
     *
     * @param type  the type of token to discard
     */
    public void discardLeading(TokenType type) {
        Token buffer = tryPeek();
        while (buffer != null && buffer.getType() == type) {
            tryRead();
            buffer = tryPeek();
        }
    }
}
