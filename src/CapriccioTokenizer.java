import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Represents a service capable of transforming Capriccio source code into a token array.
 *
 * @since 23/11/19
 * @author Saul Johnson <saul.a.johnson@gmail.com>
 */
public class CapriccioTokenizer implements Tokenizer {

    /**
     * Contains token templates for all token types recognized by the tokenizer.
     */
    private static final TokenTemplate[] tokenTemplates = new TokenTemplate[] {
            // Ignore comments.
            new TokenTemplate(";.+", TokenType.LINE_COMMENT, true),
            // Ignore all whitespace.
            new TokenTemplate("[ \\t\\r\\n]+", TokenType.WHITESPACE, true),
            // Punctuation.
            new TokenTemplate("\\(", TokenType.OPEN_PARENTHESIS),
            new TokenTemplate("\\)", TokenType.CLOSE_PARENTHESIS),
            // Free-form identifiers and integers.
            new TokenTemplate("\\b[0-9]+\\b", TokenType.INTEGER),
            new TokenTemplate("\\b[a-zA-Z_]\\w*\\b", TokenType.IDENTIFIER),
    };

    /**
     * Counts the number of newlines in a string.
     *
     * @param source    the string to count the newlines in
     * @return          the number of newlines in the string
     */
    private static int countNewlines(String source) {
        int count = 0;
        for (int i = 0; i < source.length(); i++) {
            if (source.charAt(i) == '\n'){
                count++;
            }
        }
        return count;
    }

    /**
     * Gets the current line position in the source.
     *
     * @param source    the complete source
     * @param remaining the source remaining to tokenize
     * @return          the current line position in the source
     */
    private static int getLinePosition(String source, String remaining)
    {
        return countNewlines(source) - countNewlines(remaining) + 1;
    }

    /**
     * Gets the current column position in the source.
     *
     * @param source    the complete source
     * @param remaining the source remaining to tokenize
     * @return          the current column position in the source
     */
    private static int getColumnPosition(String source, String remaining)
    {
        String processed = source.substring(0, source.length() - remaining.length());
        return processed.length() - processed.lastIndexOf('\n');
    }

    /**
     * @inheritDoc
     */
    public Token[] tokenize(String source) throws TokenizationException {

        // We're going to return an array of tokens, build it here.
        List<Token> tokens = new LinkedList<Token>();

        // Tokenize input.
        String remaining = source;
        while (remaining != null && !remaining.equals(""))
        {
            // Track position in text.
            int line = getLinePosition(source, remaining);
            int column = getColumnPosition(source, remaining);

            // Try to match each template against start of input.
            boolean matches = false;
            for (TokenTemplate tokenTemplate : tokenTemplates)
            {
                Matcher matcher = tokenTemplate.getPattern().matcher(remaining);
                if (matcher.find() && matcher.start() == 0)
                {
                    // Add token of matching type.
                    if (!tokenTemplate.isIgnored()) {
                        tokens.add(new Token(matcher.group(), tokenTemplate.getType(), line, column));
                    }

                    // Trim string from beginning of source.
                    remaining = matcher.replaceFirst("");
                    matches = true;
                    break;
                }
            }

            // Unexpected character encountered.
            if (!matches)
            {
                char character = remaining.charAt(0);
                throw new TokenizationException("Unexpected character '" + character + "' at line "+ line + " column" +
                        " " + column + ".", line, column);
            }
        }

        return tokens.toArray(new Token[] {});
    }
}
