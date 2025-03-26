package dcr1.common.data.computation;

import dcr1.common.Environment;
import dcr1.common.data.types.PrimitiveType;
import dcr1.common.data.types.Type;
import dcr1.common.data.values.BooleanVal;
import dcr1.common.data.values.IntegerVal;
import dcr1.common.data.values.StringVal;
import dcr1.common.data.values.Value;

public record EqualsExpr<T extends PrimitiveType>(ComputationExpression<T> left,
                                                  ComputationExpression<T> right)
        implements BooleanExpression {

    public static <T extends PrimitiveType> EqualsExpr<T> of(ComputationExpression<T> left,
            ComputationExpression<T> right) {
        return new EqualsExpr<>(left,right);
    }

    @Override
    public BooleanVal eval(Environment<Value<? extends Type>> env) {
        var leftVal = left.eval(env);
        var rightVal = right.eval(env);
        boolean isEqual = switch (leftVal) {
            case BooleanVal booleanVal -> booleanVal.value() == (((BooleanVal) rightVal).value());
            case IntegerVal integerVal -> integerVal.value() == (((IntegerVal) rightVal).value());
            case StringVal stringVal -> stringVal.value().equals (((StringVal)rightVal).value());
            default ->
                    throw new IllegalStateException("Internal error. Unexpected value: " + leftVal);
        };
        return BooleanVal.of(isEqual);
    }

    @Override
    public String toString() {
        return String.format("%s == %s", left, right);
    }


    @Override
    public String unparse() {
        return toString();
    }
}