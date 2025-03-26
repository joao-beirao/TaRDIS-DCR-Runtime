package dcr1.common.events.userset.values;

import dcr1.common.data.types.PrimitiveType;
import dcr1.common.data.values.Value;
import dcr1.common.events.userset.RoleParams;

import java.util.Objects;

// TODO [sanitize args]
public record RoleVal(String role, RoleParams<Value<? extends PrimitiveType>> params)
        implements UserSetVal {

    public RoleVal {
        Objects.requireNonNull(role);
        Objects.requireNonNull(params);
    }

    public static RoleVal of(String role) {
        return new RoleVal(role, RoleParams.empty());
    }

    public static RoleVal of(String role, RoleParams<Value<? extends PrimitiveType>> params) {
        return new RoleVal(role, params);
    }

    @Override
    public String toString() {
        return String.format("%s(*)", role);
    }
}
