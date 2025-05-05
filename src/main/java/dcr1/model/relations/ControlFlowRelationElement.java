package dcr1.model.relations;

import dcr1.common.data.computation.BooleanExpression;

public final class ControlFlowRelationElement
    extends GenericRelationElement
    implements IControlFlowRelationElement {



  private final String targetId;
  private final Type relationType;

  // TODO add static of() method
  // TODO[?] allow adding in bulk as of(src, tgt, EnumSet<Type>)

  ControlFlowRelationElement(String elementId, String sourceId, String targetId,
      Type relationType, BooleanExpression instantiationConstraint) {
    super(elementId, sourceId, instantiationConstraint);
    this.targetId = targetId;
    this.relationType = relationType;
  }

  ControlFlowRelationElement(String elementId, String sourceId,
          BooleanExpression guard, String targetId,
          Type relationType) {
    super(elementId, sourceId, guard);
    this.targetId = targetId;
    this.relationType = relationType;
  }

  @Override
  public String getTargetId() {
    return targetId;
  }

  @Override
  public Type getRelationType() {
    return relationType;
  }

  @Override
  public BooleanExpression instantiationConstraint() {
    return null;
  }

  @Override
  public String toString() {
    return String.format("<elemId: %s> %s %s %s", getElementId(), getSourceId(), getRelationType(),
            getTargetId());
  }

  @Override
  public String unparse() {
    return toString();
  }
}