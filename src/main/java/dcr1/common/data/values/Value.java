package dcr1.common.data.values;

import dcr1.common.data.types.EventType;
import dcr1.common.data.types.Type;

import java.io.Serializable;

// TODO [?] consider @Overriding equals across values?
public sealed interface Value<T extends Type>
        extends Serializable
        permits
        // BooleanVal,
                EventIdVal,
                EventVal,
                // IntegerVal,
                PrimitiveValue,
                RecordVal,
                // StringLiteral,
                Undefined {

    // TODO ensure this across values - any benefit of a 'default'? probably preferable to have
    //  the static version as currently - more efficient
    // default UnitVal<T> undefined() {
    //     return new UnitVal<>(this.type());
    // }


    // TODO [remove/cleanup]
    String unparse();

    T type();

    // TODO [revisit] worth it to implement here? or just leave as interface?
    // default UnitVal<T> undefined() {return new UnitVal<>(type());}

    // TODO [revisit] loosing benefits of having a static final ref at hand
    // default ConstRefVal<T> refTo() {return ConstRefVal.of(this);}

    default EventVal<T> wrap(String label) {return EventVal.of(this, toEventType(label));}

    default EventType<T> toEventType(String label) {return EventType.of(label, type());}
}
