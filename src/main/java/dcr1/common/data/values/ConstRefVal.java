package dcr1.common.data.values;

import dcr1.common.data.types.Type;

// TODO [argument check]

// "Const": cannot change val through this ref (but value can change underneath - ConstRef, not
// RefConst...)
public record ConstRefVal<T extends Type>(Value<T> value)
        implements RefValue<T> {

    public static <T extends Type> ConstRefVal<T> of(Value<T> value) {
        return new ConstRefVal<>(value);
    }

    // @Override
    // public String unparse() {
    //     throw new RuntimeException("Not yet implemented");
    // }
    //
    // @Override
    // public ConstRefType<T> type() {
    //     throw new RuntimeException("Not yet implemented");
    // }
    //
    // @Override
    // public EventVal<ConstRefType<T>> wrap(String label) {
    //     throw new RuntimeException("Not yet implemented");
    // }
}
