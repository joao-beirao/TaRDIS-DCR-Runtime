package dcr1.runtime.elements.relations;

import dcr1.runtime.elements.events.EventInstance;

public  interface ControlFlowRelationInstance extends dcr1.common.relations.ControlFlowRelation {
    EventInstance getTarget();
}
