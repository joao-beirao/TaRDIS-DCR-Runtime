package dcr1.common.events;

import dcr1.common.data.types.Type;
import dcr1.common.data.values.Value;
import dcr1.common.events.userset.expressions.UserSetExpression;

import java.util.Optional;

/**
 * An event which is initiated locally (<i>i.e.<i/>, by the owner of the projection).
 * <p>
 * A locally initiated event is said to be a <i>local event</i> if it has no passive participants,
 * and a <i>send event</i> otherwise.
 *
 * @param <T>
 *         the {@link Type type} of {@link Value value} stored by this event
 */
public interface LocallyInitiatedEvent<T extends Type>
        extends Event<T> {

    /**
     * Returns an optional describing the set of receivers of this event.
     *
     * @return an optional describing the receivers of this event, and an empty optional if there
     *         aren't any.
     */
    default Optional<? extends UserSetExpression> receivers() {
        return this.remoteParticipants();
    }
}
