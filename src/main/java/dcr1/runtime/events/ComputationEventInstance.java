package dcr1.runtime.events;

import dcr1.common.events.ComputationEvent;
import dcr1.common.data.types.Type;
import dcr1.common.events.userset.expressions.UserSetExpression;

import java.util.Optional;

public interface ComputationEventInstance<T extends Type>
        extends LocallyInitiatedEventInstance<T>, ComputationEvent<T> {
    @Override
    Optional<? extends UserSetExpression> receivers();

}