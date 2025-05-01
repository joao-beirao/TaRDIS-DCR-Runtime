package dcr1.runtime.elements.events;

import dcr1.common.events.InputEvent;
import dcr1.common.events.userset.expressions.UserSetExpression;

import java.util.Optional;

public interface InputEventInstance
    extends LocallyInitiatedEventInstance, InputEvent {
    @Override
    Optional<? extends UserSetExpression> receivers();
}