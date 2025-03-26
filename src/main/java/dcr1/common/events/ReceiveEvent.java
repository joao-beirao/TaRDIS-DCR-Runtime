package dcr1.common.events;

import dcr1.common.data.types.Type;
import dcr1.common.data.values.Value;

// TODO [javadoc]
// TODO [revisit] consider adding a getSenderExpr()

/**
 * A DCR Receive event.
 *
 * @param <T>
 *         the {@link Type type} of {@link Value value} stored by this event
 */
public interface ReceiveEvent<T extends Type>
        extends RemotelyInitiatedEvent<T> {
}
