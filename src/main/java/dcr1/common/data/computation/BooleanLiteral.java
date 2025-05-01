package dcr1.common.data.computation;

import dcr1.common.data.values.Value;
import dcr1.common.data.values.BooleanVal;
import dcr1.common.Environment;

import java.util.Objects;

public final class BooleanLiteral
        implements BooleanExpression {
    private static final BooleanLiteral TRUE = new BooleanLiteral(BooleanVal.of(true));
    private static final BooleanLiteral FALSE = new BooleanLiteral(BooleanVal.of(false));
    private final BooleanVal value;

    public static BooleanLiteral of(BooleanVal value) {
        return of(Objects.requireNonNull(value).value());
    }

    public static BooleanLiteral of(boolean value) {
        return value ? TRUE : FALSE;
    }

    private BooleanLiteral(BooleanVal value) {
        this.value = value;
    }

    @Override
    public BooleanVal eval(Environment<Value> env) {
        return value;
    }

    @Override
    public String toString() {return value.toString();}

    @Override
    public String unparse() {
        return String.format("ASTBoolean(%s)", value.unparse());
    }

}