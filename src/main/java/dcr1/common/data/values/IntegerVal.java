package dcr1.common.data.values;

import dcr1.common.data.types.IntegerType;

import java.io.Serial;
import java.util.Objects;


// TODO [revisit] maybe rename both here and ReGraDa? Number -> Integer

/**
 * An immutable integer literal {@link Value value}.
 */
public final class IntegerVal
        implements PrimitiveValue<IntegerType>, Comparable<IntegerVal> {
    @Serial
    private static final long serialVersionUID = -6289916328707077719L;
    private static final Undefined<IntegerType> UNDEFINED = new Undefined<>(IntegerType.singleton());

    private final int value;

    public static Undefined<IntegerType> undefined() {return UNDEFINED;}

    public static IntegerVal of(int value) {return new IntegerVal(value);}

    private IntegerVal(int value) {this.value = value;}

    @Override
    public IntegerType type() {return IntegerType.singleton();}

    public int value() {
        return this.value;
    }

    @Override
    public String unparse() {
        return String.format("IntegerVal(%s)", value);
    }

    @Override
    public int compareTo(IntegerVal other) {
        return Integer.compare(this.value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {return true;}
        if (obj == null) {return false;}
        if (getClass() != obj.getClass()) {return false;};
        return value == (((IntegerVal)obj).value);
    }

    public String toString() {
        return String.valueOf(value);
    }
}
