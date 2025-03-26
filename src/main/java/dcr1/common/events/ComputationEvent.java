package dcr1.common.events;

import dcr1.common.data.computation.ComputationExpression;
import dcr1.common.data.types.Type;
import dcr1.common.data.values.Value;

// TODO [javadoc]

/**
 * A DCR Computation event
 *
 * @param <T>
 *         the {@link Type type} of {@link Value value} stored by this event
 */
public interface ComputationEvent<T extends Type>
        extends LocallyInitiatedEvent<T> {
    ComputationExpression<T> getComputationExpression();

    // Optional<? extends UserSetExpression> receivers();
}


