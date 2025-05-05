package dcr1.common.data.values;

import dcr1.common.data.types.BooleanType;

import java.io.Serial;
import java.util.Objects;

/**
 * An immutable boolean literal {@link Value value}.
 */
public final class BoolVal
        implements PrimitiveVal {

    @Serial
    private static final long serialVersionUID = 3976540914858038158L;
    private static final BoolVal TRUE = new BoolVal(true);
    private static final BoolVal FALSE = new BoolVal(false);
    private final boolean value;

    // private static final Undefined<BooleanType> UNDEFINED = new Undefined<>(BooleanType.singleton());
    // private static final ConstRefVal<BooleanType> TRUE_REF = ConstRefVal.of(TRUE);
    // private static final ConstRefVal<BooleanType> FALSE_REF = ConstRefVal.of(FALSE);

    public static BoolVal of(boolean value) {
        return value ? TRUE : FALSE;
    }

    // public static Undefined<BooleanType> undefined() {
    //     return UNDEFINED;
    // }

    private BoolVal(boolean value) {this.value = value;}

    /**
     * Retrieves the boolean literal enclosed by this Value
     *
     * @return the boolean literal enclosed by this Value
     */
    public boolean value() {
        return value;
    }

    // TODO rethink this

    @Override
    public BooleanType type() {return BooleanType.singleton();}

    // @Override
    // public ConstRefVal<BooleanType> refTo() {
    //     return this == TRUE ? TRUE_REF : FALSE_REF;
    // }


    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {return true;}
        if (obj == null) {return false;}
        if (getClass() != obj.getClass()) {return false;};
        return value == (((BoolVal)obj).value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }


    @Override
    public String unparse() {
        return String.format("BooleanVal(%s)", value);
    }


}
