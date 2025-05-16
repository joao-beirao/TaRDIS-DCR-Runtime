package deprecated.dcr.ast;

import java.util.Objects;

import deprecated.dcr.ast.values.Value;

// TODO [ignore] ignore for now - exploratory attempt to factorize
abstract class ASTBinaryOp implements ASTNode {
  protected final ASTNode left;
  protected final ASTNode right;

  public ASTBinaryOp(ASTNode left, ASTNode right) {
    this.left = Objects.requireNonNull(left);
    this.right = Objects.requireNonNull(right);
  }

  public final ASTNode getLeft() {
    return left;
  }

  public final ASTNode getRight() {
    return right;
  }

  public final <T> Value eval(Environment env, Class<T> clazz) throws DynamicTypeCheckException, ArithmeticException {
    return null;
    // Value lVal = left.eval(env);
    // if (lVal.getClass().equals(clazz)) {
    //   Value rVal = right.eval(env);
    //   if (rVal.getClass().equals(clazz))
    //     return compute(clazz.cast(lVal), clazz.cast(rVal));
    //   throw new DynamicTypeCheckException(this, right, lVal.getClass(), NumberVal.class,
    //       "Binary arithmetic expression requires Number-valued expressions: found "
    //           + rVal.getClass().getSimpleName());
    // }
    // throw new DynamicTypeCheckException(this, left, lVal.getClass(), NumberVal.class,
    //     "Binary arithmetic expression requires Number-valued expressions: found "
    //         + lVal.getClass().getSimpleName());
  }

   // specialized class must define the operation it performs
   protected abstract <T> Value compute(T leftVal, T rightVal);
}
