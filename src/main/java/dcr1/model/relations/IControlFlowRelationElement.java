package dcr1.model.relations;


import dcr1.common.relations.ControlFlowRelation;

public sealed interface IControlFlowRelationElement extends RelationElement, ControlFlowRelation
    permits ControlFlowRelationElement {
}
