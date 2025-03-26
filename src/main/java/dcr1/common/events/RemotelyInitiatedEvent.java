package dcr1.common.events;

import dcr1.common.data.types.Type;
import dcr1.common.events.userset.expressions.UserSetExpression;

public interface RemotelyInitiatedEvent<T extends Type> extends Event<T> {
    UserSetExpression getSenderExpr();
}
