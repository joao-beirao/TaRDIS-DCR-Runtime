package dcr1.common.data.computation;

import dcr1.common.data.ASTComparable;
import dcr1.common.data.values.IntVal;
import dcr1.common.data.values.Value;
import dcr1.common.Environment;

import java.util.Objects;

public final class IntLiteral
        implements ComputationExpression, ASTComparable<IntVal> {

    // TODO same comment placed in NumberVal -> this just should be renamed to
    // ASTInteger (but ReGraDa would have to go first)
    private final IntVal value;

    private IntLiteral(IntVal intVal) {
        this.value = intVal;
    }

    public static IntLiteral of(IntVal value) {
        return new IntLiteral(Objects.requireNonNull(value));
    }

    public static IntLiteral of(int value) {
        return new IntLiteral(IntVal.of(value));
    }

    @Override
    public IntVal eval(Environment<Value> env) {
        return this.value;
    }

    @Override
    public String unparse() {
        return String.format("ASTNumber(%s)", value.unparse());
    }

    @Override
    public String toString() {return value.toString();}

    @Override
    public boolean isEqualTo(IntVal other) {
        return this.value.value() == other.value();
    }

}