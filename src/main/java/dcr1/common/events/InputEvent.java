package dcr1.common.events;

import dcr1.common.data.types.Type;
import dcr1.common.data.values.Value;

// TODO [javadoc]
// TODO [revisit] consider adding a getInitiator()

/**
 * A DCR Input event
 *
 * @param <T>
 *         the {@link Type type} of {@link Value value} stored by this event
 */
public interface InputEvent<T extends Type>
        extends LocallyInitiatedEvent<T> {
}