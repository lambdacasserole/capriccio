import java.util.Scanner;
import java.io.*;

public class Main {

  private static String readFileText(String path) throws FileNotFoundException {
    return new Scanner(new File(path)).useDelimiter("\\A").next();
  }

  public static void main(String[] args) {
    try {
      String source = readFileText(args[0]);
      Tokenizer tokenizer = new CapriccioTokenizer();
      TokenStream tokenStream = new TokenStream(tokenizer.tokenize(source));
      Expression program = Expression.parse(tokenStream);
      Evaluator evaluator = new Evaluator();
//      evaluator.addFunction(new collatzFunction());
//      evaluator.addFunction(new sumFunction());
      System.out.println(evaluator.evaluate(program));
    } catch (FileNotFoundException e) {

    } catch (TokenizationException e) {
      System.err.println(e.getMessage());
    } catch (ParseException e) {
      System.err.println(e.getMessage());
    } catch (EvaluationException e) {
      System.err.println(e.getMessage());
    }
  }
}
