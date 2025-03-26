package dcr1.runtime.events;

import dcr1.common.data.types.Type;
import dcr1.common.events.InputEvent;
import dcr1.common.events.userset.expressions.UserSetExpression;

import java.util.Optional;

public interface InputEventInstance<T extends Type>
    extends LocallyInitiatedEventInstance<T>, InputEvent<T> {
    @Override
    Optional<? extends UserSetExpression> receivers();
}