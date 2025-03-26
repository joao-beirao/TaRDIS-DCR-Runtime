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

// A (leaf) expression holding an event identifier (e.g., e1) leaf expression
public record EventIdExpr<T extends Type>(EventIdVal<T> eventIdLiteral)
        implements ComputationExpression<EventType<T>> {

    public static <T extends Type> EventIdExpr<T> of(EventIdVal<T> eventIdLiteral) {
        return new EventIdExpr<>(eventIdLiteral);
    }

    public EventIdExpr {Objects.requireNonNull(eventIdLiteral);}

    @Override
    // public Value<? extends ConstRefType<EventType<T>>> eval(Environment<Value<? extends Type>> env)
    public Value<? extends EventType<T>> eval(Environment<Value<?>> env)
           {
        // note: attempt to catch wrong type of value as soon they are retrieved from the
        // environment
        var lookupResult = env.lookup(eventIdLiteral.id())
                .map(Environment.Binding::value)
                .orElseThrow(() -> new IllegalStateException(
                        "Internal Error: Environment was expected to contain a binding for " +
                                "event " + eventIdLiteral.id()));
        if (lookupResult instanceof EventVal<?> eventVal) {
            // TODO wrap in try-catch-throw IllegalStateException - implementation error
            if(eventVal.type().equals(eventIdLiteral.eventType())) {
                @SuppressWarnings("unchecked") var parametrisedEventVal = (EventVal<T>) eventVal;
                return parametrisedEventVal;
            }
            throw new IllegalStateException(
                    String.format("Internal Error: unexpected type of Value for event value " +
                            "associated with id '%s'\n - got %s\n - expected %s",
                            eventIdLiteral.id(),
                            lookupResult.type(), eventIdLiteral.eventType()));
        }
        // there are currently no other Values of type EventType (something went wrong)
        throw new IllegalStateException(
                "Internal Error: expecting an EventVal value, but got " + "this instead: " +
                        lookupResult);
    }

    @Override
    public String toString() {
        return eventIdLiteral.id();
    }

    @Override
    public String unparse() {
        return String.format("EventIdLiteral(%s)", eventIdLiteral.unparse());
    }

    // TODO [discard test]
    public static void main(String[] args) {
        EventIdExpr<IntegerType> eventIdExpr = EventIdExpr.of(EventIdVal.of("e1",
                EventType.of("E1", IntegerType.singleton())));
    }
}
// package dcr1.expressions.computation;
//
// import dcr1.common.Environment;
// import dcr1.expressions.DynamicTypeCheckException;
// import dcr1.expressions.UndeclaredIdentifierException;
// import dcr1.expressions.types.ConstRefType;
// import dcr1.expressions.types.EventType;
// import dcr1.expressions.types.IntType;
// import dcr1.expressions.types.Type;
// import dcr1.expressions.values.ConstRefVal;
// import dcr1.expressions.values.EventIdVal;
// import dcr1.expressions.values.EventVal;
// import dcr1.expressions.values.Value;
//
// import java.util.Objects;
//
// // TODO generic error handling for IllegalStateException / InternalError
// // TODO sanitize args (annotations?) :
// public record EventIdExpr<T extends Type>(EventIdVal<T> eventIdLiteral)
//         implements ComputationExpression<ConstRefType<EventType<T>>> {
//
//     public static <T extends Type> EventIdExpr<T> of(EventIdVal<T> eventIdLiteral) {
//         return new EventIdExpr<>(eventIdLiteral);
//     }
//
//     public EventIdExpr {Objects.requireNonNull(eventIdLiteral);}
//
//     @Override
//     // public Value<? extends ConstRefType<EventType<T>>> eval(Environment<Value<? extends Type>> env)
//     public ConstRefVal<EventType<T>> eval(Environment<Value<?>> env)
//             throws DynamicTypeCheckException, UndeclaredIdentifierException {
//         // note: attempt to catch wrong type of value as soon they are retrieved from the
//         // environment
//         var lookupResult = env.lookup(eventIdLiteral.id())
//                 .map(Environment.Binding::value)
//                 .orElseThrow(() -> new IllegalStateException(
//                         "Internal Error: Environment was expected to contain a binding for " +
//                                 "event " + eventIdLiteral.id()));
//         if (lookupResult instanceof EventVal<?> eventVal) {
//             // TODO wrap in try-catch-throw IllegalStateException - implementation error
//             if(eventVal.type().equals(eventIdLiteral.eventType())) {
//                 @SuppressWarnings("unchecked") var parametrisedEventVal = (EventVal<T>) eventVal;
//                 return ConstRefVal.of(parametrisedEventVal);
//             }
//             throw new IllegalStateException(
//                     String.format("Internal Error: unexpected type of Value for event value " +
//                             "associated with id '%s' - got %s", eventIdLiteral.id(),
//                             lookupResult.type()));
//         }
//         // there are currently no other Values of type EventType (something went wrong)
//         throw new IllegalStateException(
//                 "Internal Error: expecting an EventVal value, but got " + "this instead: " +
//                         lookupResult);
//     }
//
//     @Override
//     public String toString() {
//         return eventIdLiteral.id();
//     }
//
//     @Override
//     public String unparse() {
//         return String.format("EventIdLiteral(%s)", eventIdLiteral.unparse());
//     }
//
//     // TODO [discard test]
//     public static void main(String[] args) {
//         EventIdExpr<IntType> eventIdExpr = EventIdExpr.of(EventIdVal.of("e1",
//                 EventType.of("E1", IntType.singleton())));
//     }
// }
