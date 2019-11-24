import java.util.List;
import java.util.LinkedList;

/**
 * Represents a service capable of evaluating Capriccio expressions.
 *
 * @since 23/11/19
 * @author Saul Johnson <saul.a.johnson@gmail.com>
 */
public class Evaluator {

    private List<NamedFunction<int[], Integer>> functions;

    /**
     * Initialises a new instance of a service capable of evaluating Capriccio expressions.
     */
    public Evaluator() {
        functions = new LinkedList<NamedFunction<int[], Integer>>();
    }

    /**
     * Adds a function to those usable from expressions evaluated by this evaluator.
     *
     * @param function  the function to add
     */
    public void addFunction(NamedFunction<int[], Integer> function) {
        functions.add(function);
    }

    /**
     * Gets the function that corresponds to the name given.
     *
     * @param name  the name
     * @return      the function
     */
    private NamedFunction<int[], Integer> getFunction(String name) {
        for (NamedFunction<int[], Integer> function : functions) {
            if (function.getName().equals(name)) {
                return function;
            }
        }
        return null;
    }

    /**
     * Evaluates an {@link Expression}.
     *
     * @param expression    the expression to evaluate
     * @return              the result of evaluation
     */
    public int evaluate(Expression expression) throws EvaluationException {

        // Is this an atomic value or a call?
        switch (expression.getType()) {
            case ATOM:
                Atom atom = (Atom) expression;
                return atom.getValue(); // An atom evaluates to itself (its own value).
            case CALL:
                // Call encountered.
                Call call = (Call) expression;

                // Evaluate subexpressions.
                Expression[] args = call.getExpressions();
                int arity = args.length;
                int[] evaluatedArgs = new int[arity];
                for (int i = 0; i < arity; i++) {
                    evaluatedArgs[i] = evaluate(args[i]);
                }

                // Attempt to retrieve function.
                Token functionNameToken = call.getToken();
                String functionName = call.getIdentifier();
                NamedFunction<int[], Integer> func = getFunction(functionName);
                if (func == null) {
                    throw new EvaluationException("Unknown function " + call.getIdentifier(),
                            functionNameToken.getLine(), functionNameToken.getColumn());
                }

                // Check function arity.
                if (func.getArity() != arity) {
                    throw new EvaluationException("Arity incorrect for call to  function " + call.getIdentifier() +
                            ". " + func.getArity() + " arguments required but " + arity + " given",
                            functionNameToken.getLine(), functionNameToken.getColumn());
                }

                // Apply function to arguments and return result.
                return func.apply(evaluatedArgs);
        }

        // Check we have a source code location.
        if (expression.getToken() != null) {
            // Expression type not known, but we have a source code location.
            throw new EvaluationException("Unknown expression",
                    expression.getToken().getLine(), expression.getToken().getColumn());
        } else {
            throw new EvaluationException("Unknown expression", 0, 0); // No token, no idea what happened.
        }
    }
}
