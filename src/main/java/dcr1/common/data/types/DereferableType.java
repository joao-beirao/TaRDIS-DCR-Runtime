package dcr1.common.data.types;

public sealed interface DereferableType extends Type
        permits EventType, RecordType {
}
