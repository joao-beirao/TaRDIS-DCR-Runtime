package dcr.runtime.elements.events;

import dcr.common.events.LocallyInitiatedEvent;
import dcr.common.events.userset.expressions.UserSetExpression;

import java.util.Optional;

public interface LocallyInitiatedEventInstance
        extends EventInstance,
                LocallyInitiatedEvent {
    @Override
    Optional<? extends UserSetExpression> receivers();
}
