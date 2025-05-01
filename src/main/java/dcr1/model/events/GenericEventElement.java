package dcr1.model.events;

import dcr1.common.data.computation.BooleanExpression;
import dcr1.common.events.userset.expressions.UserSetExpression;
import dcr1.model.GenericElement;

import java.util.Objects;
import java.util.Optional;

public sealed abstract class GenericEventElement
        extends GenericElement
        implements EventElement
        permits ComputationEvent, InputEvent, ReceiveEvent {

    private final String localId;
    private final String label;
    private final MarkingElement initialMarking;
    private final UserSetExpression passiveParticipants;
    private final BooleanExpression instantiationConstraint;
    private final BooleanExpression ifcConstraint;


    GenericEventElement(String elementId, String localId, String label,
            MarkingElement initialMarking, UserSetExpression passiveParticipants,
            BooleanExpression constraint, BooleanExpression ifcConstraint) {
        super(elementId);
        this.localId = Objects.requireNonNull(localId);
        this.label = Objects.requireNonNull(label);
        this.initialMarking = Objects.requireNonNull(initialMarking);
        this.passiveParticipants = passiveParticipants;
        this.instantiationConstraint = Objects.requireNonNull(constraint);
        this.ifcConstraint = ifcConstraint;
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
    public MarkingElement marking() {
        return initialMarking;
    }

    // TODO Revisit - make abstract, move to concrete class and adjust constructor
    @Override
    public Optional<UserSetExpression> remoteParticipants() {
        return Optional.ofNullable(passiveParticipants);
    }

    @Override
    public BooleanExpression instantiationConstraint() {
        return instantiationConstraint;
    }

    @Override
    public BooleanExpression ifcConstraint() {
        return ifcConstraint;
    }
}
