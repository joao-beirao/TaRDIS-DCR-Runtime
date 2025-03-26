package dcr1.runtime.events;

import dcr1.common.events.RemotelyInitiatedEvent;
import dcr1.common.data.types.Type;
import dcr1.common.data.values.Value;
import dcr1.common.events.userset.expressions.UserSetExpression;

/**
 * An event which is initiated remotely (<i>i.e.<i/>, not by the owner of the projection).
 *
 * @param <T>
 *         the {@link Type type} of {@link Value value} stored by this event
 */
public interface RemotelyInitiatedEventInstance<T extends Type>
        extends EventInstance<T>, RemotelyInitiatedEvent<T> {
    @Override
    UserSetExpression getSenderExpr();
}
