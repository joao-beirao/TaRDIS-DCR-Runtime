package dcr1.common.events.userset;

import dcr1.common.Record;

import java.io.Serializable;
import java.util.Objects;


// TODO (?) deprecate (new constraints make this pointless (?)
public record UserParams<V>(Record<? extends V> params) implements Serializable {

    private static final String ID_FIELD_LABEL = "id";

    // public static <V> UserParams<V> of(V id) {
    //     return new UserParams<>(Record.ofEntries(Record.Field.of(ID_FIELD_LABEL, id)));
    // }

    public static <V> UserParams<V> of(Record<? extends V> params) {
        return new UserParams<>(params);
    }

    public UserParams {
        Objects.requireNonNull(params);
        var id = params.get(ID_FIELD_LABEL).orElseThrow(() -> new IllegalStateException(
                "Internal Error: RoleParam object has been built without the " +
                        "mandatory 'id' field"));
        // TODO [validate id]
    }

    public V getId() { // note: .get() succeeds by construction
        return params.get(ID_FIELD_LABEL).get();
    }
}

// public record UserParams<V>(Record<? extends V> params) implements Serializable {
//
//     private static final String ID_FIELD_LABEL = "id";
//
//     public static <V> UserParams<V> of(V id) {
//         return new UserParams<>(Record.ofEntries(Record.Field.of(ID_FIELD_LABEL, id)));
//     }
//
//     public static <V> UserParams<V> of(Record<? extends V> params) {
//         return new UserParams<>(params);
//     }
//
//     public UserParams {
//         Objects.requireNonNull(params);
//         var id = params.get(ID_FIELD_LABEL).orElseThrow(() -> new IllegalStateException(
//                 "Internal Error: RoleParam object has been built without the " +
//                         "mandatory 'id' field"));
//         // TODO [validate id]
//     }
//
//     public V getId() { // note: .get() succeeds by construction
//         return params.get(ID_FIELD_LABEL).get();
//     }
//
// }
