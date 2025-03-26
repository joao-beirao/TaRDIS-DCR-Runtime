package dcr1.runtime.relations;

import dcr1.model.relations.ControlFlowRelationElement;
import dcr1.model.relations.IControlFlowRelationElement;
import dcr1.model.relations.RelationElement;
import dcr1.runtime.events.EventInstance;

public  interface ControlFlowRelationInstance extends dcr1.common.relations.ControlFlowRelation {
    EventInstance<?> getTarget();
}
