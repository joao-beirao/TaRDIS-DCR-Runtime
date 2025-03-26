package dcr1.common.data.types;

import java.util.Objects;

// TODO [sanitize fields]
// TODO [javadoc]

/**
 *
 * @param alias
 * @param valueType
 * @param <T>
 */
public record EventType<T extends Type>(String alias, T valueType)
        implements Type {

    public static <T extends Type> EventType<T> of(String alias, T valueType) {
        return new EventType<>(alias, valueType);
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(alias);
    }

    // note: equality-check trusts the contract, where an event's label determines its value type;
    // TODO [enforced at runtime - TypeRegister]
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {return true;}
        if (obj == null) {return false;}
        if(obj instanceof EventType<?> other) {
            return alias.equals(other.alias);
        }
        return false;
    }

    // TODO [tostring ?]
}
