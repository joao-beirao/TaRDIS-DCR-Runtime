package dcr.common.events.userset.expressions;


import dcr.common.Environment;
import dcr.common.data.values.Value;
import dcr.common.events.userset.values.UserSetVal;
import dcr.common.events.userset.values.UserVal;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

// TODO [sanitize args]

public record InitiatorExpr(String eventId) implements UserSetExpression {

    public InitiatorExpr {
        Objects.requireNonNull(eventId);
    }

    public static InitiatorExpr of(String eventId) { return new InitiatorExpr(eventId);}

    @Override
    public UserSetVal eval(Environment<Value> valueEnv, Environment<UserVal> userEnv) {
        return userEnv.lookup(eventId)
                .orElseThrow(() -> new IllegalStateException(
                        "Internal " + "Error: bad environment - missing binding for event " +
                                eventId))
                .value();
    }

    @NotNull
    @Override
    public String toString() {
        return String.format("@Initiator(%s)", eventId);
    }
}
