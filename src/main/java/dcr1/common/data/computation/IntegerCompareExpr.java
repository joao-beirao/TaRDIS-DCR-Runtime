package dcr1.common.data.computation;

import dcr1.common.Environment;
import dcr1.common.data.values.BoolVal;
import dcr1.common.data.values.IntVal;
import dcr1.common.data.values.Value;

import java.util.Objects;

// limited comparison for now, only ints; avoiding over-complicating things for now, to be
// generalised in the future as needed.
public record IntegerCompareExpr(ComputationExpression left,
                                 ComputationExpression right,
                                 CompareOp compareOp)
        implements BooleanExpression {

    public enum CompareOp {
        EQ("=="), GT(">"), GTE(">="), LT("<"), LTE("LTE");
        private final String stringVal;

        CompareOp(String stringVal) {
            this.stringVal = stringVal;
        }

        @Override
        public String toString() {
            return stringVal;
        }
    }

    public static IntegerCompareExpr ofEq(ComputationExpression left,
            ComputationExpression right) {
        return new IntegerCompareExpr(left, right, CompareOp.EQ);
    }
    public static IntegerCompareExpr ofGt(ComputationExpression left,
            ComputationExpression right) {
        return new IntegerCompareExpr(left, right, CompareOp.GT);
    }

    public static IntegerCompareExpr ofGeq(ComputationExpression left,
            ComputationExpression right) {
        return new IntegerCompareExpr(left, right, CompareOp.GTE);
    }

    public static IntegerCompareExpr ofLt(ComputationExpression left,
            ComputationExpression right) {
        return new IntegerCompareExpr(left, right, CompareOp.LT);
    }

    public static IntegerCompareExpr ofLeq(ComputationExpression left,
            ComputationExpression right) {
        return new IntegerCompareExpr(left, right, CompareOp.LTE);
    }

    public IntegerCompareExpr {
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        Objects.requireNonNull(compareOp);
    }

    @Override
    public BoolVal eval(Environment<Value> env) {
        var leftVal = left.eval(env);
        if(leftVal instanceof IntVal leftInt) {
            var rightVal = right.eval(env);
            if(rightVal instanceof IntVal rightInt) {
                return BoolVal.of(
                        switch (compareOp) {
                            case EQ -> leftInt.value() == rightInt.value();
                            case GT -> leftInt.value() > rightInt.value();
                            case GTE -> leftInt.value() >= rightInt.value();
                            case LT -> leftInt.value() < rightInt.value();
                            case LTE -> leftInt.value() <= rightInt.value();
                        }
                );
            } else {
                throw new IllegalStateException("Internal Error: comparison expecting an integer val." +
                        " Got " + rightVal);
            }
        } else {
            throw new IllegalStateException("Internal Error: comparison expecting an integer val." +
                    " Got " + leftVal);
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", left, compareOp, right);
    }

    @Override
    public String unparse() {
        return toString();
    }
}
