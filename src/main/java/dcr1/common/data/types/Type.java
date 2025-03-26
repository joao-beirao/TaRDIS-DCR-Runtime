package dcr1.common.data.types;

import java.io.Serializable;

// TODO [javadoc] note: all implementors expected to override hashcode, equals, and toString
public sealed interface Type
        extends Serializable
        permits PrimitiveType,
        // ConstRefType,
                EventIdType,
                EventType,
                PrimitiveTy,
                RecordType
        // RefType,
{}
