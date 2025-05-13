package dcr1.common.data.computation;

import dcr1.common.data.values.Value;
import dcr1.common.data.values.BoolVal;
import dcr1.common.Environment;

import java.util.Objects;

public final class BoolLiteral
        implements ComputationExpression {
    public static final BoolLiteral TRUE = new BoolLiteral(BoolVal.of(true));
    public static final BoolLiteral FALSE = new BoolLiteral(BoolVal.of(false));
    private final BoolVal value;

    public static BoolLiteral of(BoolVal value) {
        return of(Objects.requireNonNull(value).value());
    }

    public static BoolLiteral of(boolean value) {
        return value ? TRUE : FALSE;
    }

    private BoolLiteral(BoolVal value) {
        this.value = value;
    }

    @Override
    public BoolVal eval(Environment<Value> env) {
        return value;
    }

    @Override
    public String toString() {return value.toString();}

}