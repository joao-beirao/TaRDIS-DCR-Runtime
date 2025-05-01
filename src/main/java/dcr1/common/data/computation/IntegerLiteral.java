package dcr1.common.data.computation;

import dcr1.common.data.ASTComparable;
import dcr1.common.data.types.IntegerType;
import dcr1.common.data.values.IntegerVal;
import dcr1.common.data.values.Value;
import dcr1.common.Environment;

import java.util.Objects;

public final class IntegerLiteral
        implements ComputationExpression, ASTComparable<IntegerVal> {

    // TODO same comment placed in NumberVal -> this just should be renamed to
    // ASTInteger (but ReGraDa would have to go first)
    private final IntegerVal value;

    private IntegerLiteral(IntegerVal integerVal) {
        this.value = integerVal;
    }

    public static IntegerLiteral of(IntegerVal value) {
        return new IntegerLiteral(Objects.requireNonNull(value));
    }

    public static IntegerLiteral of(int value) {
        return new IntegerLiteral(IntegerVal.of(value));
    }

    @Override
    public IntegerVal eval(Environment<Value> env) {
        return this.value;
    }

    @Override
    public String unparse() {
        return String.format("ASTNumber(%s)", value.unparse());
    }

    @Override
    public String toString() {return value.toString();}

    @Override
    public boolean isEqualTo(IntegerVal other) {
        return this.value.value() == other.value();
    }

}