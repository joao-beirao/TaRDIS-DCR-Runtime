package dcr1.model.events;

import dcr1.common.data.computation.BooleanExpression;
import dcr1.common.data.computation.ComputationExpression;
import dcr1.common.events.userset.expressions.UserSetExpression;
import dcr1.runtime.elements.events.ComputationEventInstance;

import java.util.Optional;
import java.util.function.Function;

public sealed interface ComputationEventElement
        extends EventElement, dcr1.common.events.ComputationEvent
        permits ComputationEvent {

    @Override
    Optional<UserSetExpression> receivers();
}

final class ComputationEvent
        extends GenericEventElement
        implements ComputationEventElement {
    private final ComputationExpression computationExpression;
    Function<ComputationEvent, ComputationEventInstance> builder;

    ComputationEvent(String elementId, String localId, String label,
            ComputationExpression computationExpression, MarkingElement marking,
            UserSetExpression receivers, BooleanExpression instantiationConstraint,
            BooleanExpression ifcConstraint) {
        super(elementId, localId, label, marking, receivers, instantiationConstraint, ifcConstraint);
        this.computationExpression = computationExpression;
    }

    @Override
    public ComputationExpression getComputationExpression() {
        return computationExpression;
    }

    @Override
    public Optional<UserSetExpression> receivers() {
        return remoteParticipants();
    }

    @Override
    public String toString() {
        return String.format("<%s> %s(%s: %s) [%s] (%s) [-> %s]", getElementId(),
                this.marking().toStringPrefix(), localId(), label(), getComputationExpression(),
                value(), receivers());
    }

    @Override
    public String unparse() {
        return String.format("ComputationEventElement<%s>[ (%s: %s) [%s] [%s] ]", getElementId(),
                localId(), label(), getComputationExpression(), receivers());
    }
}


