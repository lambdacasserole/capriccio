/**
 * Represents a service capable of transforming source code into a token array.
 *
 * @since 23/11/19
 * @author Saul Johnson <saul.a.johnson@gmail.com>
 */
public interface Tokenizer {

    /**
     * Converts a source code string into a token array.
     * @param source                    the source code string to convert
     * @return                          the resulting token array
     * @throws TokenizationException    if an unexpected character is encountered
     */
    Token[] tokenize(String source) throws TokenizationException;
}
