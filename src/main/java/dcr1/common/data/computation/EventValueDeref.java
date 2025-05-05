package dcr1.common.data.computation;


import dcr1.common.Environment;
import dcr1.common.data.types.Type;
import dcr1.common.data.values.EventVal;
import dcr1.common.data.values.Value;

import java.util.Objects;

// Current implementation assumes that a computation expression only dereferences the value
// property of an Event - which is the only one currently supported
// TODO [sanitize args]
public record EventValueDeref(
        ComputationExpression eventExpr)
        implements ComputationExpression {

    public static <T extends Type> EventValueDeref of(
            ComputationExpression eventExpr) {
        return new EventValueDeref(eventExpr);
    }

    public EventValueDeref {Objects.requireNonNull(eventExpr);}

    // TODO wrap in try-catch-throw->IllegalStateException - flag implementation error
    @Override
    public Value eval(Environment<Value> env) {
        var evalResult = eventExpr.eval(env);
        if (evalResult instanceof EventVal eventVal) {
            return (Value) eventVal.value();
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
    // public static void main(String[] args) {
    //     TypeRegister.register("E1", IntegerType.singleton());
    //     EventIdExpr<IntegerType> eventIdExpr =
    //             EventIdExpr.of(EventIdVal.of("e1", EventType.of("E1", IntegerType.singleton())));
    //     var deref = new EventValueDeref<>(eventIdExpr);
    //     Environment<Value<?>> env = Environment.empty();
    //     env.bindIfAbsent("e1",
    //             EventVal.of(IntegerVal.of(2), EventType.of("E1", IntegerType.singleton())));
    //     System.err.println(eventIdExpr);
    //     System.err.println(deref);
    //     System.err.println(deref.eval(env));
    //     System.err.println(deref.eval(env).type());
    // }
}
