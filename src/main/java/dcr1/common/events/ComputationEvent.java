package dcr1.common.events;

import dcr1.common.data.computation.ComputationExpression;

// TODO [javadoc]

/**
 * A DCR Computation event
 */
public interface ComputationEvent
        extends LocallyInitiatedEvent {
    ComputationExpression getComputationExpression();
}


