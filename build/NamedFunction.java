import java.util.function.Function;

/**
 * Represents a named function, tagged with its arity.
 *
 * @since 23/11/19
 * @author Saul Johnson <saul.a.johnson@gmail.com>
 * @param <T> the function input type
 * @param <R> the function output type
 */
public interface NamedFunction<T, R> extends Function<T, R> {

    /**
     * Gets the function name.
     *
     * @return the function name
     */
    String getName();

    /**
     * Gets the function arity (number of arguments taken).
     *
     * @return the function arity
     */
    int getArity();
}
