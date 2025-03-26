package dcr1.common.data.values;

import dcr1.common.data.types.GenericStringType;

public sealed interface StringLiteral
        extends PrimitiveValue<GenericStringType>
        permits StringVal {
    String value();

    @Override
    default String unparse() { return value(); };
}
