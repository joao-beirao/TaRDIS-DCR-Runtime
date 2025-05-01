package dcr1.common.data.values;

public sealed interface StringLiteral
        extends PrimitiveVal
        permits StringVal {
    String value();

    @Override
    default String unparse() { return value(); };


}
