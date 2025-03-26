package dcr1.model.events;

import dcr1.common.data.computation.BooleanExpression;
import dcr1.common.data.computation.ComputationExpression;
import dcr1.common.data.types.Type;
import dcr1.common.events.userset.expressions.UserSetExpression;

public final class EventElements {

    public static <T extends Type> ComputationEventElement<T> newComputationEvent(String elementId,
            String localId, String label, ComputationExpression<T> computation,
            EventElement.MarkingElement<T> initialMarking, UserSetExpression receivers, BooleanExpression constraint) {
        return new ComputationEvent<>(elementId, localId, label, computation, initialMarking,
                receivers, constraint);
    }

    public static <T extends Type> ComputationEventElement<T> newLocalComputationEvent(
            String elementId, String localId, String label, ComputationExpression<T> computation,
            EventElement.MarkingElement<T> initialMarking, BooleanExpression constraint) {
        return new ComputationEvent<>(elementId, localId, label, computation, initialMarking,
                null, constraint);
    }

    public static <T extends Type> InputEventElement<T> newInputEvent(String elementId,
            String localId, String label, UserSetExpression receivers,
            EventElement.MarkingElement<T> initialMarking, BooleanExpression constraint) {
        return new InputEvent<T>(elementId, localId, label, initialMarking, receivers, constraint);
    }

    public static <T extends Type> InputEventElement<T> newLocalInputEvent(String elementId,
            String localId, String label, EventElement.MarkingElement<T> initialMarking, BooleanExpression constraint) {
        return new InputEvent<T>(elementId, localId, label, initialMarking, null, constraint);
    }

    public static <T extends Type> ReceiveEventElement<T> newReceiveEvent(String elementId,
            String localId, String label, UserSetExpression senders,
            EventElement.MarkingElement<T> initialMarking, BooleanExpression constraint) {
        return new ReceiveEvent<>(elementId, localId, label, senders, initialMarking, constraint);
    }
}