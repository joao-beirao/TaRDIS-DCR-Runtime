package dcr1.common.relations;

import dcr1.common.data.computation.BoolLiteral;
import dcr1.common.data.computation.ComputationExpression;

// TODO [javadoc]
public interface Relation {
    ComputationExpression DEFAULT_GUARD = BoolLiteral.TRUE;
    ComputationExpression DEFAULT_INSTANTIATION_CONSTRAINT = BoolLiteral.TRUE;

    String sourceId();
    ComputationExpression guard();
    ComputationExpression instantiationConstraint();
}
