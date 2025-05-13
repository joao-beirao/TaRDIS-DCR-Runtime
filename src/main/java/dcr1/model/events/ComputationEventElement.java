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

    ComputationEvent(String choreoElementUID, String endpointElementUID, String localId, String eventType,
            ComputationExpression computationExpression, MarkingElement marking,
            UserSetExpression receivers, BooleanExpression instantiationConstraint,
            BooleanExpression ifcConstraint) {
        super(choreoElementUID, endpointElementUID, localId, eventType, marking, receivers,
                instantiationConstraint,
                ifcConstraint);
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
        return String.format("<%s, %s> %s(%s: %s) [%s] (%s) [%s] (when: %s)", choreoElementUID(),
                endpointElementUID(),
                this.marking().toStringPrefix(), remoteID(), label(), getComputationExpression(),
                value(), receivers().map(r -> String.format("@self -> %s", r)).orElse(
                "Local"), instantiationConstraint());
    }

    @Override
    public String unparse() {
        return String.format("ComputationEventElement<%s>[ (%s: %s) [%s] [%s] ]", choreoElementUID(),
                remoteID(), label(), getComputationExpression(), receivers());
    }
}


