package dcr1.common.events.userset;

import dcr1.common.Record;

import java.util.Objects;
import java.util.Optional;

// TODO make abstract
abstract class UserSetParams<V> {
    // private final Record<? extends V> params;
    //
    // public UserSetParams(Record<? extends V> params) {
    //     this.params = Objects.requireNonNull(params);
    // }
    // public Optional<? extends V> get(String name) {
    //     return params.get(name);
    // }
    //
    // public Record<? extends V> fields() {
    //     return this.params;
    // }
    //
    // @Override
    // public String toString() {
    //     return params.toString();
    // }
}
