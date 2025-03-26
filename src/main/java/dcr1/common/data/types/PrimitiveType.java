package dcr1.common.data.types;

public sealed interface PrimitiveType extends Type
        permits VoidType, BooleanType, GenericStringType, IntegerType, StringType {

    // TODO move default hashcode and equals in here
}
