package dcr1.model.relations;


import dcr1.common.relations.ControlFlowRelation;

import javax.management.relation.RelationType;

public sealed interface ControlFlowRelationElement
        extends RelationElement, ControlFlowRelation
        permits ControlFlowElement {
}
