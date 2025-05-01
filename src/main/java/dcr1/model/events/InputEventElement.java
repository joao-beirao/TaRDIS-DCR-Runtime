package dcr1.model.events;

import dcr1.common.data.computation.BooleanExpression;
import dcr1.common.events.userset.expressions.UserSetExpression;

import java.util.Optional;

// TODO  implement [?] - not accepting empty-input events yet

public sealed interface InputEventElement
        extends EventElement, dcr1.common.events.InputEvent
        permits InputEvent {
    @Override
    Optional<UserSetExpression> receivers();
}

final class InputEvent
        extends GenericEventElement
        implements InputEventElement {
    InputEvent(String elementId, String localId, String label,
            MarkingElement marking, UserSetExpression receivers, BooleanExpression instantiationConstraint,
            BooleanExpression ifcConstraint) {
        super(elementId, localId, label, marking, receivers, instantiationConstraint, ifcConstraint);
    }

    @Override
    public Optional<UserSetExpression> receivers() {
        return remoteParticipants();
    }

    @Override
    public String toString() {
        return String.format("<%s> %s(%s: %s) [?:%s] (%s) [%s] ", getElementId(),
                this.marking().toStringPrefix(),
                localId(), label(), valueType(), value(), receivers());
    }

    // FIXME '?:' only when type expr not empty; '?' otherwise
    @Override
    public String unparse() {
        return String.format("InputEventElement<%s>[ (%s: %s) [?:%s] [%s] ]", getElementId(),
                localId(), label(), valueType(), receivers());
    }
}

