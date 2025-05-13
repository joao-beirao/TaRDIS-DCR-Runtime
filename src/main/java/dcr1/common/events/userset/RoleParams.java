package dcr1.common.events.userset;

import dcr1.common.Record;

import java.io.Serializable;
import java.util.Objects;

// TODO deprecate?
public record RoleParams<V>(Record<? extends V> params) implements Serializable {

    public static <V> RoleParams<V> of(Record<? extends V> params) {
        return new RoleParams<>(params);
    }


    public static <V> RoleParams<V> empty() {
        return new RoleParams<>(Record.empty());
    }

    public RoleParams {Objects.requireNonNull(params);}
}