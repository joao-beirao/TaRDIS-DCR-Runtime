package deprecated.dcr.ast;

import deprecated.dcr.ast.values.RefVal;
import deprecated.dcr.ast.values.Value;

public final class ASTDeref implements ASTNode {

  private final ASTNode refNode;

  public ASTDeref(ASTNode refNode) {
    this.refNode = refNode;
  }

  @Override
  public Value eval(Environment env) throws DynamicTypeCheckException, UndeclaredIdentifierException {
    Value val = refNode.eval(env);
    if (val instanceof RefVal) {
      return ((RefVal) val).getValue();
    }
    // TODO [revisit] typecheker should maki this unnecessary
    throw new DynamicTypeCheckException(refNode, refNode, val.getClass(), RefVal.class,
        "Can only deref RefVals: found " + val.getClass().getSimpleName());
  }

  @Override
  public String unparse() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'unparse'");
  }

}
