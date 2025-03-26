package dcr1.common.data.values;

import dcr1.common.data.types.EventIdType;
import dcr1.common.data.types.EventType;
import dcr1.common.data.types.Type;

// TODO validate args - (reminder -> proper event id)

// TODO [reminder] reuse eventType refs - many EventIdVal instances but, comparatively, very few
//  event types - and a ref type

// TODO [revisit] should EventIdType be <T> parametrised? Not just label?
public record EventIdVal<T extends Type>(String id, EventType<T> eventType)
        implements Value<EventIdType> {

    public static <T extends Type> EventIdVal<T> of(String id,
            EventType<T> eventType) {return new EventIdVal<>(id, eventType);}

    public static <T extends Type> Undefined<EventIdType> undefined(String label) {
        return new Undefined<>(EventIdType.of(label));
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {return true;}
        if (obj == null) {return false;}
        if (getClass() != obj.getClass()) {return false;};
        return id.equals(((EventIdVal<?>)obj).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {return id;}

    @Override
    public String unparse() {
        return String.format("EventIdVal(%s)", id);
    }

    @Override
    public EventIdType type() {
        throw new RuntimeException("Not yet implemented");
    }

}


// public record EventIdVal<T extends Type>(String id, RefType<EventType<T>> refType)
//         implements Value<RefType<EventType<T>>> {
//
//     public static <T extends Type> EventIdVal<T> of(String eventId, RefType<EventType<T>>
//     refType) {
//         return new EventIdVal<T>(eventId, refType);
//     }
//
//     @Override
//     public String toString() {return id;}
//
//     @Override
//     public String unparse() {
//         return String.format("EventIdVal(%s)", id);
//     }
//
//     @Override
//     public RefType<EventType<T>> type() {
//         return refType;
//     }
// }
