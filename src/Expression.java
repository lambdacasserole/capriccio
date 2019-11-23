public abstract class Expression {

private ExpressionType type;

  public Expression(ExpressionType type){
    this.type = type;
  }

  public ExpressionType getType() {
    return type;
  }

  /**
   * Reads the tokens comprising an expression from the given token stream and returns the parsed result.
   *
   * @param tokenStream       the token stream to read from
   * @return                  the parsed result
   * @throws ParseException   if parsing fails
   */
  public static Expression parse(TokenStream tokenStream) throws ParseException {

        // Peek expecting one of two possibilities.
        TokenType[] possibilities = new TokenType[] {TokenType.OPEN_PARENTHESIS, TokenType.INTEGER};
        switch (tokenStream.peekExpectingOneOf(possibilities).getType()) {
            case OPEN_PARENTHESIS:
                return Call.parse(tokenStream); // An open parenthesis indicates a call.
            case INTEGER:
                return Atom.parse(tokenStream);
            default:
                return null; // Should not be triggered.
        }
  }
}
