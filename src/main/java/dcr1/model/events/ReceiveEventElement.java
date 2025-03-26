package dcr1.model.events;

import dcr1.common.data.computation.BooleanExpression;
import dcr1.common.data.types.Type;
import dcr1.common.events.userset.expressions.UserSetExpression;

public sealed interface ReceiveEventElement<T extends Type>
        extends EventElement<T>, dcr1.common.events.ReceiveEvent<T>
        permits ReceiveEvent {

    UserSetExpression getSenderExpr();
}

final class ReceiveEvent<T extends Type>
        extends GenericEventElement<T>
        implements ReceiveEventElement<T> {

    ReceiveEvent(String elementId, String localId, String label,
            UserSetExpression senders, MarkingElement<T> marking, BooleanExpression constraint) {
        super(elementId, localId, label, marking, senders, constraint);
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
                this.marking().toStringPrefix(), localId(), label(), valueType(), getValue(),
                this.getSenderExpr());
    }

    @Override
    public String unparse() {
        return String.format("ReceiveEventElement<%s>[ (%s: %s) [%s] [%s] ]", getElementId(),
                localId(), label(), valueType(), this.getSenderExpr());
    }
}
