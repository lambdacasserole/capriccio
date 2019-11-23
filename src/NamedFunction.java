import java.util.function.Function;

public interface NamedFunction<T, R> extends Function<T, R> {

  String getName();

}
