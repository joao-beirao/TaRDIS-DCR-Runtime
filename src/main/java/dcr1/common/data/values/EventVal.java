package dcr1.common.data.values;

import dcr1.common.data.types.EventType;
import dcr1.common.data.types.Type;

// TODO remove type - add to Value interface a default so that a value can return the EventType
//  of an Event enclosing it
public record EventVal<T extends Type>(Value<? extends T> value, EventType<T> type)
        implements Value<EventType<T>> {

    // TODO 1. replace *EventType<T> type*  with *String label*
    // TODO 2. implement type() returning value.toEventType(this.label)


    public static <T extends Type> EventVal<T> of(Value<T> value, EventType<T> type) {
        return new EventVal<>(value, type);
    }

    public static <T extends Type> Undefined<EventType<T>> undefined(String label, T type) {
        return new Undefined<>(EventType.of(label, type));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {return true;}
        if (obj == null) {return false;}
        if (getClass() != obj.getClass()) {return false;};
        return value.equals(((EventVal<?>)obj).value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s:[value:%s]", type.alias(), value);
    }

    @Override
    public String unparse() {
        return String.format("EventVal(%s, :%s)", type().alias(), value.unparse());
    }
}
