package deprecated.dcr.ast;

import deprecated.dcr.ast.values.BooleanVal;
import deprecated.dcr.ast.values.NumberVal;
import deprecated.dcr.ast.values.Value;

public class ASTLessThan extends ASTBinaryLogicalOp {

  public ASTLessThan(ASTNode left, ASTNode right) {
    super(left, right);
  }

@Override
public String toString() {
  return String.format("%s < %s", left, right);
}

  @Override
  public String unparse() {
    return String.format("ASTLessThan(%s, %s)", left.unparse(), right.unparse());
  }

  @Override
  protected Value compute(NumberVal leftVal, NumberVal rightVal) {
    return new BooleanVal(leftVal.getValue() < rightVal.getValue());
  }

}
