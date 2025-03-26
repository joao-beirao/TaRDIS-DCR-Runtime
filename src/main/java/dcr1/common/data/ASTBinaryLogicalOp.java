package dcr1.common.data;

import dcr1.common.data.computation.BooleanExpression;
import dcr1.common.data.computation.ComputationExpression;
import dcr1.common.data.types.IntegerType;
import dcr1.common.data.values.BooleanVal;
import dcr1.common.data.values.IntegerVal;
import dcr1.common.data.values.Value;
import dcr1.common.Environment;

import java.util.Objects;

/**
 * A general binary arithmetic operation.
 */
abstract class ASTBinaryLogicalOp implements BooleanExpression {

    protected final ComputationExpression<IntegerType> left;
    protected final ComputationExpression<IntegerType> right;

    public ASTBinaryLogicalOp(ComputationExpression<IntegerType> left, ComputationExpression<IntegerType> right) {
        this.left = Objects.requireNonNull(left);
        this.right = Objects.requireNonNull(right);
    }

    public final ComputationExpression<IntegerType> getLeft() {
        return left;
    }

    public final ComputationExpression<IntegerType> getRight() {
        return right;
    }


    @Override
    public final BooleanVal eval(Environment<Value<?>> env) {
        Value<? extends IntegerType> lVal = left.eval(env);
        if (lVal instanceof IntegerVal) {
            Value<? extends IntegerType> rVal = right.eval(env);
            if (rVal instanceof IntegerVal)
                return compute((IntegerVal) lVal, (IntegerVal) rVal);
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
    protected abstract BooleanVal compute(IntegerVal leftVal, IntegerVal rightVal);
}
