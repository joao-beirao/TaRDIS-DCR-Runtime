package dcr1.common.data.computation;

import dcr1.common.Environment;
import dcr1.common.data.types.EventType;
import dcr1.common.data.types.IntegerType;
import dcr1.common.data.types.Type;
import dcr1.common.data.values.EventIdVal;
import dcr1.common.data.values.EventVal;
import dcr1.common.data.values.Value;

import java.util.Objects;

// TODO generic error handling for IllegalStateException / InternalError
// TODO sanitize args (annotations?) :
// TODO rename (EventIdLiteral more accurate)

// A (leaf) expression holding an event identifier (e.g., e1)
public record EventIdExpr(EventIdVal eventIdLiteral)
        implements PropBasedExpr {

    public static EventIdExpr of(EventIdVal eventIdLiteral) {
        return new EventIdExpr(eventIdLiteral);
    }

    public EventIdExpr {Objects.requireNonNull(eventIdLiteral);}

    @Override
    // public Value<? extends ConstRefType<EventType<T>>> eval(Environment<Value<? extends Type>> env)
    public EventVal eval(Environment<Value> env) {
        // note: attempt to catch wrong type of value as soon they are retrieved from the
        // environment
        return (EventVal) env.lookup(eventIdLiteral.id())
                .map(Environment.Binding::value)
                .orElseThrow(() -> new IllegalStateException(
                        "Internal Error: Environment was expected to contain a binding for " +
                                "event " + eventIdLiteral.id()));
        // var lookupResult = env.lookup(eventIdLiteral.id())
        //         .map(Environment.Binding::value)
        //         .orElseThrow(() -> new IllegalStateException(
        //                 "Internal Error: Environment was expected to contain a binding for " +
        //                         "event " + eventIdLiteral.id()));
        // if (lookupResult instanceof EventVal eventVal) {
        //     // TODO wrap in try-catch-throw IllegalStateException - implementation error
        //     if(eventVal.type().equals(eventIdLiteral.eventType())) {
        //         @SuppressWarnings("unchecked") var parametrisedEventVal = (EventVal<T>) eventVal;
        //         return parametrisedEventVal;
        //     }
        //     throw new IllegalStateException(
        //             String.format("Internal Error: unexpected type of Value for event value " +
        //                     "associated with id '%s'\n - got %s\n - expected %s",
        //                     eventIdLiteral.id(),
        //                     lookupResult.type(), eventIdLiteral.eventType()));
        // }
        // // there are currently no other Values of type EventType (something went wrong)
        // throw new IllegalStateException(
        //         "Internal Error: expecting an EventVal value, but got " + "this instead: " +
        //                 lookupResult);
    }

    @Override
    public String toString() {
        return eventIdLiteral.id();
    }

    @Override
    public String unparse() {
        return String.format("EventIdLiteral(%s)", eventIdLiteral.unparse());
    }
}