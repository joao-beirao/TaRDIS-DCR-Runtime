package deprecated.dcr.ast;

import java.util.Objects;

import deprecated.dcr.ast.values.BooleanVal;
import deprecated.dcr.ast.values.NumberVal;
import deprecated.dcr.ast.values.StringVal;
import deprecated.dcr.ast.values.Value;

// TODO [revisit] quick implementation just to get the example moving - this should extend binary ops - need better modeling
public class ASTEquals implements ASTNode {

  protected final ASTNode left;
  protected final ASTNode right;

  public ASTEquals(ASTNode left, ASTNode right) {
    this.left = Objects.requireNonNull(left);
    this.right = Objects.requireNonNull(right);
  }

  public final ASTNode getLeft() {
    return left;
  }

  public final ASTNode getRight() {
    return right;
  }

 public final Value eval(Environment env) throws DynamicTypeCheckException, UndeclaredIdentifierException {
    Value lVal = left.eval(env);
    if (lVal instanceof NumberVal) {
      Value rVal = right.eval(env);
      if (rVal instanceof NumberVal)
        return new BooleanVal(((NumberVal) lVal).compareTo((NumberVal) rVal) == 0);

      throw new DynamicTypeCheckException(this, right, rVal.getClass(), NumberVal.class,
          "Binary arithmetic expression requires Number-valued expressions: found "
              + rVal.getClass().getSimpleName());
    } else if (lVal instanceof StringVal) {
      Value rVal = right.eval(env);
      if (rVal instanceof StringVal)
        return new BooleanVal(((StringVal) lVal).compareTo((StringVal) rVal) == 0);

        System.err.println("Equals left node had type " + lVal.getClass().getSimpleName());
      throw new DynamicTypeCheckException(this, right, rVal.getClass(), StringVal.class,
          String.format(
              "Binary arithmetic expression requires matching expressions: found String-valued expression: found % and %s",
              rVal.getClass().getSimpleName(), lVal.getClass().getSimpleName()));
    }
    // throw new DynamicTypeCheckException(this, left, lVal.getClass(), null,
    // "Binary logical expression requires compatible expressions")

    throw new DynamicTypeCheckException(this, left, lVal.getClass(), null,
        "Binary logical expression requires Number-valued or String-valued expressions: found "
            + lVal.getClass().getSimpleName());
  }

  // public final Value eval(Environment env) throws DynamicTypeCheckException, UndeclaredIdentifierException {
  //   Value lVal = left.eval(env);
  //   if (lVal instanceof NumberVal) {
  //     Value rVal = right.eval(env);
  //     if (rVal instanceof NumberVal)
  //       return new BooleanVal(((NumberVal) lVal).compareTo((NumberVal) rVal) == 0);

  //     throw new DynamicTypeCheckException(this, right, rVal.getClass(), NumberVal.class,
  //         "Binary arithmetic expression requires Number-valued expressions: found "
  //             + rVal.getClass().getSimpleName());
  //   } else if (lVal instanceof StringVal) {
  //     Value rVal = right.eval(env);
  //     if (rVal instanceof StringVal)
  //       return new BooleanVal(((StringVal) lVal).compareTo((StringVal) rVal) == 0);

  //     throw new DynamicTypeCheckException(this, right, rVal.getClass(), StringVal.class,
  //         String.format(
  //             "Binary arithmetic expression requires matching expressions: found String-valued expression: found % and %s",
  //             rVal.getClass().getSimpleName(), lVal.getClass().getSimpleName()));
  //   }
  //   // throw new DynamicTypeCheckException(this, left, lVal.getClass(), null,
  //   // "Binary logical expression requires compatible expressions")

  //   throw new DynamicTypeCheckException(this, left, lVal.getClass(), null,
  //       "Binary logical expression requires Number-valued or String-valued expressions: found "
  //           + lVal.getClass().getSimpleName());
  // }

  @Override
  public String toString() {
    return String.format("ASTEquals(%s, %s)", left, right);
  }

  @Override
  public String unparse() {
    return String.format("%s == %s", left, right);
  }

}
