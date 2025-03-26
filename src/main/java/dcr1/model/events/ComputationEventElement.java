package dcr1.model.events;

import dcr1.common.data.computation.BooleanExpression;
import dcr1.common.data.computation.ComputationExpression;
import dcr1.common.data.types.Type;
import dcr1.common.events.userset.expressions.UserSetExpression;
import dcr1.runtime.events.ComputationEventInstance;

import java.util.Optional;
import java.util.function.Function;

public sealed interface ComputationEventElement<T extends Type>
        extends EventElement<T>, dcr1.common.events.ComputationEvent<T>
        permits ComputationEvent {

    @Override
    Optional<UserSetExpression> receivers();
}

final class ComputationEvent<T extends Type>
        extends GenericEventElement<T>
        implements ComputationEventElement<T> {
    private final ComputationExpression<T> computationExpression;
    Function<ComputationEvent<T>, ComputationEventInstance<T>> builder;

    ComputationEvent(String elementId, String localId, String label,
            ComputationExpression<T> computationExpression, MarkingElement<T> marking,
            UserSetExpression receivers, BooleanExpression constraint) {
        super(elementId, localId, label, marking, receivers, constraint);
        this.computationExpression = computationExpression;
    }

    @Override
    public ComputationExpression<T> getComputationExpression() {
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
                getValue(), receivers());
    }

    @Override
    public String unparse() {
        return String.format("ComputationEventElement<%s>[ (%s: %s) [%s] [%s] ]", getElementId(),
                localId(), label(), getComputationExpression(), receivers());
    }
}


