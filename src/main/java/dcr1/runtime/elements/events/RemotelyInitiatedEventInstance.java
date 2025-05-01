package dcr1.runtime.elements.events;

import dcr1.common.events.RemotelyInitiatedEvent;
import dcr1.common.events.userset.expressions.UserSetExpression;

/**
 * An event which is initiated remotely (<i>i.e.<i/>, not by the owner of the projection).
 */
public interface RemotelyInitiatedEventInstance
        extends EventInstance, RemotelyInitiatedEvent {
    @Override
    UserSetExpression getSenderExpr();
}
