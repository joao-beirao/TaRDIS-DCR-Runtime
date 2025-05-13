package dcr1.common.data.values;

import dcr1.common.data.types.Type;

import java.io.Serializable;

// TODO [?] consider @Overriding equals across values?
public sealed interface Value
        extends Serializable
        permits EventIdVal, PrimitiveVal, PropBasedVal, UndefinedVal, VoidVal {
    Type type();
}
