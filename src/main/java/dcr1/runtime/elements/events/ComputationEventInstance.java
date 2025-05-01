package dcr1.runtime.elements.events;

import dcr1.common.events.ComputationEvent;
import dcr1.common.events.userset.expressions.UserSetExpression;

import java.util.Optional;

public interface ComputationEventInstance
        extends LocallyInitiatedEventInstance, ComputationEvent {
    @Override
    Optional<? extends UserSetExpression> receivers();

}