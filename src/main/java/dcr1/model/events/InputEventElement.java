package dcr1.model.events;

import dcr1.common.data.computation.BooleanExpression;
import dcr1.common.data.types.Type;
import dcr1.common.events.userset.expressions.UserSetExpression;

import java.util.Optional;

// TODO  implement [?] - not accepting empty-input events yet

public sealed interface InputEventElement<T extends Type>
        extends EventElement<T>, dcr1.common.events.InputEvent<T>
        permits InputEvent {
    @Override
    Optional<UserSetExpression> receivers();
}

final class InputEvent<T extends Type>
        extends GenericEventElement<T>
        implements InputEventElement<T> {
    InputEvent(String elementId, String localId, String label,
            MarkingElement<T> marking, UserSetExpression receivers, BooleanExpression constraint) {
        super(elementId, localId, label, marking, receivers, constraint);
    }

    @Override
    public Optional<UserSetExpression> receivers() {
        return remoteParticipants();
    }

    @Override
    public String toString() {
        return String.format("<%s> %s(%s: %s) [?:%s] (%s) [%s] ", getElementId(),
                this.marking().toStringPrefix(),
                localId(), label(), valueType(), getValue(), receivers());
    }

    // FIXME '?:' only when type expr not empty; '?' otherwise
    @Override
    public String unparse() {
        return String.format("InputEventElement<%s>[ (%s: %s) [?:%s] [%s] ]", getElementId(),
                localId(), label(), valueType(), receivers());
    }
}

