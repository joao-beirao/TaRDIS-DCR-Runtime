package dcr1.model.relations;

import dcr1.common.data.computation.BooleanExpression;

// TODO [revise]
public final class RelationElements {

  public static IControlFlowRelationElement newControlFlowRelation(String elementId, String sourceId, String targetId,
      ControlFlowRelationElement.Type relationType, BooleanExpression instantiationConstraint) {
    return new ControlFlowRelationElement(elementId, sourceId, targetId, relationType, instantiationConstraint);
  }
}
