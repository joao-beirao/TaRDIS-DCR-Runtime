package dcr1.common.events;

import dcr1.common.events.userset.expressions.UserSetExpression;

public interface RemotelyInitiatedEvent
        extends Event {
    UserSetExpression getSenderExpr();
}
