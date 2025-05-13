package dcr1.model.relations;


import dcr1.common.relations.Relation;
import dcr1.model.ModelElement;

public sealed interface RelationElement
        extends Relation, ModelElement
        permits ControlFlowRelationElement, SpawnRelationElement {}
