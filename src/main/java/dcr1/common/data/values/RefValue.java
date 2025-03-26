package dcr1.common.data.values;

import dcr1.common.data.types.Type;

public sealed interface RefValue<T extends Type>
        // extends Value<ConstRefType<T>>
        permits ConstRefVal {
}
