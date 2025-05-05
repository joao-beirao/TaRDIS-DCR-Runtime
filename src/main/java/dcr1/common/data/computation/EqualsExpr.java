package dcr1.common.data.computation;

import dcr1.common.Environment;
import dcr1.common.data.values.BoolVal;
import dcr1.common.data.values.IntVal;
import dcr1.common.data.values.StringVal;
import dcr1.common.data.values.Value;
import org.jetbrains.annotations.NotNull;

public record EqualsExpr(ComputationExpression left,
                         ComputationExpression right)
        implements BooleanExpression {

    public static EqualsExpr of(ComputationExpression left,
            ComputationExpression right) {
        return new EqualsExpr(left, right);
    }

    @Override
    public BoolVal eval(Environment<Value> env) {
        var leftVal = left.eval(env);
        var rightVal = right.eval(env);
        boolean isEqual = switch (leftVal) {
            case BoolVal booleanVal -> booleanVal.value() == (((BoolVal) rightVal).value());
            case IntVal integerVal -> integerVal.value() == (((IntVal) rightVal).value());
            case StringVal stringVal -> stringVal.value().equals(((StringVal) rightVal).value());
            default ->
                    throw new IllegalStateException("Internal error. Unexpected value: " + leftVal);
        };
        return BoolVal.of(isEqual);
    }

    @NotNull
    @Override
    public String toString() {
        return String.format("%s == %s", left, right);
    }


    @Override
    public String unparse() {
        return toString();
    }
}