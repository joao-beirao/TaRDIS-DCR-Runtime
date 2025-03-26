package dcr1.model.relations;

// TODO [revise]
public final class RelationElements {

  public static IControlFlowRelationElement newControlFlowRelation(String elementId, String sourceId, String targetId,
      ControlFlowRelationElement.Type relationType) {
    return new ControlFlowRelationElement(elementId, sourceId, targetId, relationType);
  }
}
