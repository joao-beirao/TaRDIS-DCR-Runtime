package dcr1.model.events;

import dcr1.common.data.computation.BooleanExpression;
import dcr1.common.events.userset.expressions.UserSetExpression;

public sealed interface ReceiveEventElement
        extends EventElement, dcr1.common.events.ReceiveEvent
        permits ReceiveEvent {

    UserSetExpression getSenderExpr();
}

final class ReceiveEvent
        extends GenericEventElement
        implements ReceiveEventElement {

    ReceiveEvent(String elementId, String localId, String label,
            UserSetExpression senders, MarkingElement marking,
            BooleanExpression instantiationConstraint,
            BooleanExpression ifcConstraint) {
        super(elementId, localId, label, marking, senders, instantiationConstraint, ifcConstraint);
    }

    // FIXME [.get()]
    @Override
    public UserSetExpression getSenderExpr() {
        // TODO [revisit] get()
        return remoteParticipants().get();
    }

    @Override
    public String toString() {
        return String.format("<%s> %s(%s: %s) [%s] (%s) [%s -> ] ", getElementId(),
                this.marking().toStringPrefix(), localId(), label(), valueType(), value(),
                this.getSenderExpr());
    }

    @Override
    public String unparse() {
        return String.format("ReceiveEventElement<%s>[ (%s: %s) [%s] [%s] ]", getElementId(),
                localId(), label(), valueType(), this.getSenderExpr());
    }
}
