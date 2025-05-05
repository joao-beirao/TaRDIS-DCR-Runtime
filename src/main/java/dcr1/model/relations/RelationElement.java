package dcr1.model.relations;


import dcr1.common.data.computation.BooleanExpression;
import dcr1.common.data.computation.BoolLiteral;
import dcr1.common.relations.Relation;
import dcr1.model.GenericElement;
import dcr1.model.ModelElement;

public sealed interface RelationElement
        extends Relation, ModelElement
        permits GenericRelationElement, IControlFlowRelationElement, ISpawnRelationElement {}

sealed abstract class GenericRelationElement
        extends GenericElement
        implements RelationElement
        permits ControlFlowRelationElement, SpawnRelationElement {

    private static final BooleanExpression DEFAULT_GUARD = BoolLiteral.of(true);

    private final String sourceEventId;
    private final BooleanExpression guard;
    private final BooleanExpression instantiationConstraint;

    GenericRelationElement(String elementId, String sourceEventId,
            BooleanExpression instantiationConstraint) {
        super(elementId);
        this.sourceEventId = sourceEventId;
        this.guard = DEFAULT_GUARD;
        this.instantiationConstraint = instantiationConstraint;
    }

    GenericRelationElement(String elementId, String sourceEventId, BooleanExpression guard,
            BooleanExpression instantiationConstraint) {
        super(elementId);
        this.sourceEventId = sourceEventId;
        this.guard = guard;
        this.instantiationConstraint = instantiationConstraint;
    }

    @Override
    public String getSourceId() {
        return sourceEventId;
    }

    @Override
    public BooleanExpression getGuard() {
        return guard;
    }
}