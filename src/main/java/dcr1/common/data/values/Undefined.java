package dcr1.common.data.values;

import dcr1.common.data.types.Type;
import dcr1.common.data.types.VoidType;

import java.io.Serial;

/**
 * A special {@link Value} denoting the absence of value (or an undefined value) for a given type
 * (the  value of any event which has not yet executed and for which an initial default value has
 * not been provided.
 */
public final class Undefined<T extends Type>
        implements Value<T> {

    @Serial
    private static final long serialVersionUID = -1588707935989188517L;
    private static final Undefined<VoidType> VOID = new Undefined<>(VoidType.instance());
    private static final String TO_STRING_VAL = "<undefined>";

    //  TODO [revise] any workaround to avoid storing typeInstance? thinking no
    private final T typeInstance;

    public static Undefined<VoidType> ofVoid() {
        return VOID;
    }

    public static <T extends Type> Undefined<T> of(T typeInstance) {
        return new Undefined<>(typeInstance);
    }

    Undefined(T typeInstance) {
        this.typeInstance = typeInstance;
    }

    public T type() {
        return typeInstance;
    }

    @Override
    public int hashCode() {
        return typeInstance.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {return false;}
        if (this == obj) {return true;}
        if (getClass() != obj.getClass()) {return false;}
        return typeInstance.equals(((Undefined<?>) obj).typeInstance);
    }

    @Override
    public String toString() {return TO_STRING_VAL;}

    @Override
    public String unparse() {
        return String.format("UnitVal<%s>", typeInstance);
    }
}
