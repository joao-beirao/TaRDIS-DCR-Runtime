package dcr1.runtime.relations;

import dcr1.model.relations.RelationElement;
import dcr1.runtime.events.EventInstance;

public  interface RelationInstance extends dcr1.common.relations.Relation {
    EventInstance<?> getSource();
}
