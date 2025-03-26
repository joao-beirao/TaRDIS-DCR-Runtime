package dcr1.common.data.values;

import dcr1.common.data.types.PrimitiveType;

public sealed interface PrimitiveValue<T extends PrimitiveType> extends Value<T>
        permits BooleanVal, IntegerVal, StringLiteral {
}
