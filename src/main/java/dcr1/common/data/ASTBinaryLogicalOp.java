package dcr1.common.data;

import dcr1.common.data.computation.BooleanExpression;
import dcr1.common.data.computation.ComputationExpression;
import dcr1.common.data.values.BoolVal;
import dcr1.common.data.values.IntVal;
import dcr1.common.data.values.Value;
import dcr1.common.Environment;

import java.util.Objects;

/**
 * A general binary arithmetic operation.
 */
abstract class ASTBinaryLogicalOp implements BooleanExpression {

    protected final ComputationExpression left;
    protected final ComputationExpression right;

    public ASTBinaryLogicalOp(ComputationExpression left, ComputationExpression right) {
        this.left = Objects.requireNonNull(left);
        this.right = Objects.requireNonNull(right);
    }

    public final ComputationExpression getLeft() {
        return left;
    }

    public final ComputationExpression getRight() {
        return right;
    }


    @Override
    public final BoolVal eval(Environment<Value> env) {
        Value lVal = left.eval(env);
        if (lVal instanceof IntVal) {
            Value rVal = right.eval(env);
            if (rVal instanceof IntVal)
                return compute((IntVal) lVal, (IntVal) rVal);
            // TODO
            throw new RuntimeException("Not yet implemented");
            // throw new DynamicTypeCheckException(this, right, lVal.getClass(), IntegerVal.class,
            //         "Binary arithmetic expression requires Number-valued expressions: found "
            //                 + rVal.getClass().getSimpleName());
        }
        // else if(lVal instanceof StringVal) {

        // }
        // throw new DynamicTypeCheckException(this, left, lVal.getClass(), null, "Binary logical expression requires compatible expressions")
// TODO
        throw new RuntimeException("Not yet implemented");
        // throw new DynamicTypeCheckException(this, left, lVal.getClass(), IntegerVal.class,
        //         "Binary logical expression requires Number-valued expressions: found "
        //                 + lVal.getClass().getSimpleName());
    }

    // specialized class must define the operation it performs
    protected abstract BoolVal compute(IntVal leftVal, IntVal rightVal);
}
