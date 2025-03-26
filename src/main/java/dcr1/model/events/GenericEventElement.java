package dcr1.model.events;

import dcr1.common.data.computation.BooleanExpression;
import dcr1.common.data.types.Type;
import dcr1.common.events.userset.expressions.UserSetExpression;
import dcr1.model.GenericElement;

import java.util.Optional;

public sealed abstract class GenericEventElement<T extends Type>
        extends GenericElement
        implements EventElement<T>
        permits ComputationEvent, InputEvent, ReceiveEvent {

    private final String localId;
    private final String label;
    private final MarkingElement<T> initialMarking;
    private final UserSetExpression passiveParticipants;
    private final BooleanExpression constraint;


    GenericEventElement(String elementId, String localId, String label,
            MarkingElement<T> initialMarking, UserSetExpression passiveParticipants,
            BooleanExpression constraint) {
        super(elementId);
        this.localId = localId;
        this.label = label;
        this.initialMarking = initialMarking;
        this.passiveParticipants = passiveParticipants;
        this.constraint = constraint;
    }

    @Override
    public String localId() {
        return localId;
    }

    @Override
    public String label() {
        return label;
    }

    @Override
    public MarkingElement<T> marking() {
        return initialMarking;
    }

    @Override
    public Optional<UserSetExpression> remoteParticipants() {
        return Optional.ofNullable(passiveParticipants);
    }

    @Override
    public BooleanExpression constraint() {
        return constraint;
    }
}
