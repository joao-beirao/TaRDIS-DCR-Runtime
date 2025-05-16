package dcr.common.data.computation;

import dcr.common.Environment;
import dcr.common.data.values.EventIdVal;
import dcr.common.data.values.PropBasedVal;
import dcr.common.data.values.Value;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

// TODO generic error handling for IllegalStateException / InternalError
// TODO sanitize args (annotations?) :
// TODO rename (EventIdLiteral more accurate)

// A (leaf) expression holding an event identifier (e.g., e1)
public record RefExpr(EventIdVal eventIdLiteral)
        implements PropBasedExpr {

    public static RefExpr of(EventIdVal eventIdLiteral) {
        return new RefExpr(eventIdLiteral);
    }

    public static RefExpr of(String eventId) {
        return new RefExpr(EventIdVal.of(eventId));
    }

    public RefExpr {Objects.requireNonNull(eventIdLiteral);}

    @Override
    // public Value<? extends ConstRefType<EventType<T>>> eval(Environment<Value<? extends Type>> env)
    public PropBasedVal eval(Environment<Value> env) {
        // note: attempt to catch wrong type of value as soon they are retrieved from the
        // environment
        return (PropBasedVal) env.lookup(eventIdLiteral.id())
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

    @NotNull
    @Override
    public String toString() {
        return eventIdLiteral.id();
    }

}