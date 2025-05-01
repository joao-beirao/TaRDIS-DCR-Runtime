package dcr1.runtime.elements.events;

import dcr1.common.events.LocallyInitiatedEvent;
import dcr1.common.events.userset.expressions.UserSetExpression;

import java.util.Optional;

public interface LocallyInitiatedEventInstance
        extends EventInstance,
                LocallyInitiatedEvent {
    @Override
    Optional<? extends UserSetExpression> receivers();
}
