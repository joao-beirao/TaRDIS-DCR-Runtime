package dcr1.common.events.userset.expressions;


import dcr1.common.Environment;
import dcr1.common.data.values.Value;
import dcr1.common.events.userset.values.UserSetVal;
import dcr1.common.events.userset.values.UserVal;

import java.util.Objects;

// TODO [sanitize args]

public record Sender(String eventId) implements UserSetExpression {

    public Sender {
        Objects.requireNonNull(eventId);
    }

    public static Sender of(String eventId) { return new Sender(eventId);}

    @Override
    public UserSetVal eval(Environment<Value> valueEnv, Environment<UserVal> userEnv) {
        return userEnv.lookup(eventId)
                .orElseThrow(() -> new IllegalStateException(
                        "Internal " + "Error: bad environment - missing binding for event " +
                                eventId))
                .value();
    }
}
