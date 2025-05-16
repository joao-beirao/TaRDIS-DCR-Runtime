package deprecated.dcr.ast;

import deprecated.dcr.ast.values.RefVal;
import deprecated.dcr.ast.values.Value;

public class ASTVar implements ASTNode {

  private ASTNode expr;

  public ASTVar(ASTNode expr) {
    this.expr = expr;
  }

  @Override
  public Value eval(Environment env) throws UndeclaredIdentifierException, DynamicTypeCheckException {
    return new RefVal(this.expr.eval(env));
  }

  @Override
  public String toString() {
    return String.format("ASTVar(%s)", this.expr.toString());
  }

  @Override
  public String unparse() {
    return this.expr.unparse();
  }
}