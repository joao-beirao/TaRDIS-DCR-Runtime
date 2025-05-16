package deprecated.dcr.ast;

import java.util.Objects;

import deprecated.dcr.ast.values.NumberVal;
import deprecated.dcr.ast.values.Value;

/**
 * A general binary arithmetic operation.
 */
abstract class ASTBinaryArithmeticOp implements ASTNode {

    protected final ASTNode left;
    protected final ASTNode right;

    public ASTBinaryArithmeticOp(ASTNode left, ASTNode right) {
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
                return compute((NumberVal) lVal, (NumberVal) rVal);
            throw new DynamicTypeCheckException(this, right, lVal.getClass(), NumberVal.class,
                    "Binary arithmetic expression requires Number-valued expressions: found "
                            + rVal.getClass().getSimpleName());
        }
        throw new DynamicTypeCheckException(this, left, lVal.getClass(), NumberVal.class,
                "Binary arithmetic expression requires Number-valued expressions: found "
                        + lVal.getClass().getSimpleName());
    }

    // specialized class must define the operation it performs
    protected abstract Value compute(NumberVal leftVal, NumberVal rightVal);
}
