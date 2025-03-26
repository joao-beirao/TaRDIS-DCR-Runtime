package dcr1.common.data.types;

public sealed interface DereferableType<T extends Type> extends Type
        permits EventType, RecordType {
}
