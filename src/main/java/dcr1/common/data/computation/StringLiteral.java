package dcr1.common.data.computation;

import dcr1.common.data.ASTComparable;
import dcr1.common.data.types.StringType;
import dcr1.common.data.values.StringVal;
import dcr1.common.data.values.Value;
import dcr1.common.Environment;

import java.util.Objects;

public class StringLiteral
        implements ComputationExpression, ASTComparable<StringVal> {

    private static final StringLiteral EMPTY_STRING = new StringLiteral(StringVal.empty());

    private final StringVal value;

    private StringLiteral(StringVal value) {
        this.value = value;
    }

    public static StringLiteral of(StringVal value) {
        return Objects.requireNonNull(value).value().isEmpty()
                ? EMPTY_STRING
                : new StringLiteral(value);
    }

    public static StringLiteral of(String value) {
        return Objects.requireNonNull(value).isEmpty()
                ? EMPTY_STRING
                : new StringLiteral(StringVal.of(value));
    }

    public static StringLiteral empty() {
        return EMPTY_STRING;
    }

    @Override
    public StringVal eval(Environment<Value> env) {
        return value;
    }

    @Override
    public String toString() {return value.toString();}

    @Override
    public String unparse() {return String.format("StringLiteral(%s)", value.unparse());}

    @Override
    public boolean isEqualTo(StringVal other) {
        return value.value().equals(other.value());
    }
}
