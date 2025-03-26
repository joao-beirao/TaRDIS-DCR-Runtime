package dcr1.runtime.events;

import dcr1.common.data.types.Type;
import dcr1.common.events.Event;

// Marking is mutable, has a globalId/UUID, participant exprs are a fragment of the same exprs in
// the model - upon instantiation, Receiver/Sender exprs are replaced with actual User
// TODO [seal?]
public interface EventInstance<T extends Type>
        extends Event<T> {
    String getGlobalId();
}

