package dcr.common.events.userset.values;

import dcr.common.data.values.PrimitiveVal;
import dcr.common.events.userset.RoleParams;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

// TODO [sanitize args]
public record RoleVal(String role, RoleParams<PrimitiveVal> params)
        implements UserSetVal {

    public RoleVal {
        Objects.requireNonNull(role);
        Objects.requireNonNull(params);
    }

    public static RoleVal of(String role) {
        return new RoleVal(role, RoleParams.empty());
    }

    // TODO make this a Record<PrimitiveVal>
    public static RoleVal of(String role, RoleParams<PrimitiveVal> params) {
        return new RoleVal(role, params);
    }

    @NotNull
    @Override
    public String toString() {
        return String.format("%s(%s)", role, params);
    }
}
