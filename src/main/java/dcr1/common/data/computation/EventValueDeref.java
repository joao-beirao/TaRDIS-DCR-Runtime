package dcr1.common.data.computation;


import dcr1.common.Environment;
import dcr1.common.TypeRegister;
import dcr1.common.data.types.EventType;
import dcr1.common.data.types.IntegerType;
import dcr1.common.data.types.Type;
import dcr1.common.data.values.EventIdVal;
import dcr1.common.data.values.EventVal;
import dcr1.common.data.values.IntegerVal;
import dcr1.common.data.values.Value;

import java.util.Objects;

// Current implementation assumes that a computation expression only dereferences the value
// property of an Event - which is the only one currently supported
// TODO [sanitize args]
public record EventValueDeref<T extends Type>(
        ComputationExpression<? extends EventType<? extends T>> eventExpr)
        implements ComputationExpression<T> {

    public static <T extends Type> EventValueDeref<T> of(
            ComputationExpression<? extends EventType<? extends T>> eventExpr) {
        return new EventValueDeref<>(eventExpr);
    }

    public EventValueDeref {Objects.requireNonNull(eventExpr);}

    // TODO wrap in try-catch-throw->IllegalStateException - flag implementation error
    @Override
    public Value<T> eval(Environment<Value<? extends Type>> env) {
        var evalResult = eventExpr.eval(env);
        if (evalResult instanceof EventVal<?> eventVal) {
            @SuppressWarnings("unchecked") var value = (Value<T>) eventVal.value();
            return value;
        }
        // there are currently no other Values of EventType
        throw new IllegalStateException(
                "Internal Error: expecting an EventVal value, but got " + "this instead: " +
                        evalResult);
    }


    @Override
    public String toString() {
        return String.format("%s.value", eventExpr);
    }

    @Override
    public String unparse() {
        return String.format("EventValueDeref(%s)", eventExpr.unparse());
    }

    // TODO [discard test]
    public static void main(String[] args) {
        TypeRegister.register("E1", IntegerType.singleton());
        EventIdExpr<IntegerType> eventIdExpr =
                EventIdExpr.of(EventIdVal.of("e1", EventType.of("E1", IntegerType.singleton())));
        var deref = new EventValueDeref<>(eventIdExpr);
        Environment<Value<?>> env = Environment.empty();
        env.bindIfAbsent("e1", EventVal.of(IntegerVal.of(2), EventType.of("E1", IntegerType.singleton())));
        System.err.println(eventIdExpr);
        System.err.println(deref);
        System.err.println(deref.eval(env));
        System.err.println(deref.eval(env).type());
    }
}

// package dcr1.expressions.computation;
//
// // Current implementation assumes that a computation expression only dereferences the value
// // property of an Event - which is the only one currently supported
//
// import dcr1.common.Environment;
// import dcr1.common.TypeRegister;
// import dcr1.expressions.DynamicTypeCheckException;
// import dcr1.expressions.UndeclaredIdentifierException;
// import dcr1.expressions.types.*;
// import dcr1.expressions.values.EventIdVal;
// import dcr1.expressions.values.EventVal;
// import dcr1.expressions.values.IntegerVal;
// import dcr1.expressions.values.Value;
// import dcr1.expressions.values.ConstRefVal;
//
//
// public final class EventValueDeref<T extends Type>
//         implements ComputationExpression<T> {
//     private final ComputationExpression<? extends RefType<EventType<T>>> eventRef;
//
//     public EventValueDeref(
//             ComputationExpression<? extends RefType<EventType<T>>> eventRef) {
//         this.eventRef = eventRef;
//     }
//
//                 // TODO wrap in try-catch-throw->IllegalStateException - flag implementation
//                  error
//     @Override
//     public Value<T> eval(Environment<Value<? extends Type>> env)
//             throws DynamicTypeCheckException, UndeclaredIdentifierException {
//         var evalResult = eventRef.eval(env);
//         // this should be an instance of refVal and we need to extract the value out of it and
//         // return it
//
//         // if (evalResult instanceof ConstRefVal<?> ref)
//         if (evalResult.getClass().equals(ConstRefVal.class))
//         {
//             var ref = (ConstRefVal) evalResult;
//             if(ref.value() instanceof EventVal<?> event) {
//                 @SuppressWarnings("unchecked") var value = (Value<T>) event.value();
//                 return value;
//             }
//         }
//         // there are currently no other Values of type EventType (something went wrong)
//         throw new IllegalStateException("Internal Error: expecting an EventVal value, but got " +
//                 "this instead: " + evalResult);
//     }
//
//
//     @Override
//     public String toString() {
//         return String.format("%s.value", eventRef);
//     }
//
//     @Override
//     public String unparse() {
//         return String.format("EventValueDeref(%s)", eventRef.unparse());
//     }
//
//     // TODO [discard test]
//     public static void main(String[] args)
//             throws UndeclaredIdentifierException, DynamicTypeCheckException {
//         TypeRegister.register("E1", IntType.singleton());
//         EventIdExpr<IntType> eventIdExpr = EventIdExpr.of(EventIdVal.of("e1",
//                 EventType.of("E1", IntType.singleton())));
//         var deref = new EventValueDeref<>(eventIdExpr);
//         Environment<Value<?>> env = Environment.empty();
//         env.bind("e1", EventVal.of(IntegerVal.of(2), EventType.of("E1", IntType.singleton())));
//         System.err.println(eventIdExpr);
//         System.err.println(deref);
//         System.err.println(deref.eval(env));
//     }
// }
