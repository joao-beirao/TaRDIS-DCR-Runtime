package dcr.common.data.values;

import dcr.common.data.types.EventType;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;


// TODO remove type - add to Value interface a default so that a value can return the EventType
//  of an Event enclosing it
public record EventVal(Value value, EventType type)
        implements PropBasedVal {

    // TODO 1. replace *EventType<T> type*  with *String label*
    // TODO 2. implement type() returning value.toEventType(this.label)


    public static EventVal of(Value value, EventType type) {
        return new EventVal(value, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {return true;}
        if (obj == null) {return false;}
        if (getClass() != obj.getClass()) {return false;};
        return value.equals(((EventVal)obj).value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @NotNull
    @Override
    public String toString() {
        return String.format("%s:[value:%s]", type.typeAlias(), value);
    }

    @Override
    public Value fetchProp(String propName) {
        throw new NotImplementedException("TODO");
    }
}
