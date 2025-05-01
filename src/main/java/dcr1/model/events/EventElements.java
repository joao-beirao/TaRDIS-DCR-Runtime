package dcr1.model.events;

import dcr1.common.data.computation.BooleanExpression;
import dcr1.common.data.computation.ComputationExpression;
import dcr1.common.events.userset.expressions.UserSetExpression;

public final class EventElements {

    public static ComputationEventElement newComputationEvent(String elementId,
            String localId, String label, ComputationExpression computation,
            EventElement.MarkingElement initialMarking, UserSetExpression receivers,
            BooleanExpression instantiationConstraint, BooleanExpression ifcConstraint) {
        return new ComputationEvent(elementId, localId, label, computation, initialMarking,
                receivers, instantiationConstraint, ifcConstraint);
    }

    public static ComputationEventElement newLocalComputationEvent(
            String elementId, String localId, String label, ComputationExpression computation,
            EventElement.MarkingElement initialMarking, BooleanExpression instantiationConstraint,
            BooleanExpression ifcConstraint) {
        return new ComputationEvent(elementId, localId, label, computation, initialMarking,
                null, instantiationConstraint, ifcConstraint);
    }

    public static InputEventElement newInputEvent(String elementId,
            String localId, String label, UserSetExpression receivers,
            EventElement.MarkingElement initialMarking, BooleanExpression instantiationConstraint,
            BooleanExpression ifcConstraint) {
        return new InputEvent(elementId, localId, label, initialMarking, receivers, instantiationConstraint, ifcConstraint);
    }

    public static InputEventElement newLocalInputEvent(String elementId,
            String localId, String label, EventElement.MarkingElement initialMarking,
            BooleanExpression instantiationConstraint,
            BooleanExpression ifcConstraint) {
        return new InputEvent(elementId, localId, label, initialMarking, null, instantiationConstraint,
                ifcConstraint);
    }

    public static ReceiveEventElement newReceiveEvent(String elementId,
            String localId, String label, UserSetExpression senders,
            EventElement.MarkingElement initialMarking, BooleanExpression instantiationConstraint,
            BooleanExpression ifcConstraint) {
        return new ReceiveEvent(elementId, localId, label, senders, initialMarking, instantiationConstraint, ifcConstraint);
    }
}