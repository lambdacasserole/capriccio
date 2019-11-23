import java.util.List;
import java.util.LinkedList;

public class Evaluator {

  private List<NamedFunction<int[], Integer>> functions;

  public Evaluator() {
    functions = new LinkedList<NamedFunction<int[], Integer>>();
  }

  public void addFunction(NamedFunction<int[], Integer> function) {
    functions.add(function);
  }

  private NamedFunction<int[], Integer> getFunction(String name) {
    for (NamedFunction<int[], Integer> function : functions) {
      if (function.getName().equals(name)) {
        return function;
      }
    }
    return null;
  }

  public int evaluate(Expression expression) {
    switch(expression.getType()) {
      case ATOM:
        Atom atom = (Atom) expression;
        return atom.getValue();
      case CALL:
        Call call = (Call) expression;
        Expression[] args = call.getExpressions();
        int arity = args.length;
        int[] evaluatedArgs = new int[arity];
        for (int i = 0; i < arity; i++) {
          evaluatedArgs[i] = evaluate(args[i]);
        }
        NamedFunction<int[], Integer> func = getFunction(call.getIdentifier());
        if (func == null) {
          return 0; // TODO: Error.
        }
        return func.apply(evaluatedArgs);
    }
    return 0; // TODO: Error.
  }
}
