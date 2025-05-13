package dcr1.runtime.elements.relations;

import dcr1.common.data.computation.ComputationExpression;
import dcr1.model.relations.RelationElement;
import dcr1.runtime.elements.RuntimeElement;
import dcr1.runtime.elements.events.EventInstance;

public  interface RelationInstance
        extends RuntimeElement, dcr1.common.relations.Relation {
    EventInstance getSource();

    @Override
    RelationElement baseElement();

    default ComputationExpression instantiationConstraint() {
        return baseElement().instantiationConstraint();
    }
}
