package dcr1.runtime.events;

import dcr1.common.events.LocallyInitiatedEvent;
import dcr1.common.data.types.Type;
import dcr1.common.events.userset.expressions.UserSetExpression;

import java.util.Optional;

public interface LocallyInitiatedEventInstance<T extends Type>
        extends EventInstance<T>,
                LocallyInitiatedEvent<T> {
    @Override
    Optional<? extends UserSetExpression> receivers();
}
