import java.util.regex.Pattern;

/**
 * Represents a token template.
 *
 * @since 23/11/19
 * @author Saul Johnson <saul.a.johnson@gmail.com>
 */
@SuppressWarnings({"WeakerAccess", "unused"}) // API class.
public class TokenTemplate {

    private Pattern pattern;

    private TokenType type;

    private boolean ignored;

    /**
     * Initialises a new instance of a token template.
     *
     * @param pattern   the regular expression that describes the token
     * @param type      the type of the tokens this template describes
     * @param ignored   whether or not tokens matching this template should be omitted from the token stream
     */
    public TokenTemplate(Pattern pattern, TokenType type, boolean ignored) {
        this.pattern = pattern;
        this.type = type;
        this.ignored = ignored;
    }

    /**
     * Initialises a new instance of a token template.
     *
     * @param pattern   the regular expression that describes the token, given as a string
     * @param type      the type of the tokens this template describes
     * @param ignored   whether or not tokens matching this template should be omitted from the token stream
     */
    public TokenTemplate(String pattern, TokenType type, boolean ignored) {
        this(Pattern.compile(pattern), type, ignored);
    }

    /**
     * Initialises a new instance of a token template.
     *
     * @param pattern   the regular expression that describes the token, given as a string
     * @param type      the type of the tokens this template describes
     */
    public TokenTemplate(String pattern, TokenType type) {
        this(Pattern.compile(pattern), type, false);
    }

    /**
     * Gets the regular expression that describes the token.
     *
     * @return  the regular expression
     */
    public Pattern getPattern() {
        return pattern;
    }

    /**
     * Gets the type of the tokens this template describes.
     *
     * @return  the token type
     */
    public TokenType getType() {
        return type;
    }


    /**
     * Gets whether or not tokens matching this template should be omitted from the token stream.
     *
     * @return  true if tokens should be omitted, otherwise false
     */
    public boolean isIgnored() {
        return ignored;
    }
}
