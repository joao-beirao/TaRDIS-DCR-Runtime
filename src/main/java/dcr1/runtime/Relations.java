package dcr1.runtime;

import dcr1.common.data.computation.BooleanExpression;
import dcr1.model.GraphModel;
import dcr1.model.relations.ControlFlowRelationElement;
import dcr1.model.relations.IControlFlowRelationElement;
import dcr1.model.relations.ISpawnRelationElement;
import dcr1.model.relations.RelationElement;
import dcr1.runtime.relations.ControlFlowRelationInstance;
import dcr1.runtime.relations.RelationInstance;
import dcr1.runtime.relations.SpawnRelationInstance;

class Relations {
    static InstantiatedControlFlowRelation newControlFlowRelation(
            IControlFlowRelationElement baseElement, GenericEventInstance<?> source,
            GenericEventInstance<?> target) {
        return new InstantiatedControlFlowRelation(baseElement, source, target);
    }

    static InstantiatedSpawnRelation newSpawnRelationInstance(ISpawnRelationElement baseElement,
            GenericEventInstance<?> source) {
        return new InstantiatedSpawnRelation(baseElement, source);
    }
}

abstract class InstantiatedRelation
        implements RelationInstance {

    // TODO [revise] storing baseElement here - it seems we have to map everything but the relation
    //  type; then again, we are still missing the guard, which might be something to eval
    private final RelationElement baseElement;
    private final GenericEventInstance<?> source;


    InstantiatedRelation(RelationElement baseElement, GenericEventInstance<?> source) {
        this.baseElement = baseElement;
        this.source = source;
    }

    @Override
    public String getSourceId() {
        return source.getGlobalId();
    }

    @Override
    public BooleanExpression getGuard() {
        return baseElement.getGuard();
    }

    @Override
    public GenericEventInstance<?> getSource() {
        return source;
    }

    protected RelationElement getBaseElement() {
        return baseElement;
    }
}


class InstantiatedSpawnRelation
        extends InstantiatedRelation
        implements SpawnRelationInstance {

    InstantiatedSpawnRelation(ISpawnRelationElement baseElement, GenericEventInstance<?> source) {
        super(baseElement, source);
    }

    @Override
    public GraphModel getSubgraph() {
        return ((ISpawnRelationElement) getBaseElement()).getSubgraph();
    }

    // TODO [not yet implemented]
    @Override
    public String toString() {
        return super.toString();
    }

    // TODO [not yet implemented]
    public String unparse(String indentation) {
        return super.toString();
        //
        // return String.format("%s%s -->> {\n%s%s\n}", indentation, getSourceId(),
        //         indentation + "  ",
        //         ((ISpawnRelationElement) getBaseElement()).getSubgraph().unparse(indentation+"  "));
    }
}


final class InstantiatedControlFlowRelation
        extends InstantiatedRelation
        implements ControlFlowRelationInstance {

    private final GenericEventInstance<?> target;

    InstantiatedControlFlowRelation(IControlFlowRelationElement baseElement,
            GenericEventInstance<?> source,
            GenericEventInstance<?> target) {
        super(baseElement, source);
        this.target = target;
    }

    @Override
    public String getTargetId() {
        return target.getGlobalId();
    }

    @Override
    public GenericEventInstance<?> getTarget() {
        return target;
    }

    @Override
    public ControlFlowRelationElement.Type getRelationType() {
        return ((IControlFlowRelationElement) getBaseElement()).getRelationType();
    }

    // FIXME
    // TODO use relation type label value to get a one liner
    @Override
    public String toString() {
        return unparse("");
    }

    //
    public String unparse(String indentation) {
        return switch (getRelationType()) {
            case INCLUDE -> String.format("%s%s -->+ %s", indentation, getSourceId(),
                    getTargetId());
            case EXCLUDE -> String.format("%s%s -->%% %s", indentation, getSourceId(),
                    getTargetId());
            case RESPONSE -> String.format("%s%s *--> %s", indentation, getSourceId(),
                    getTargetId());
            case CONDITION -> String.format("%s%s -->* %s", indentation, getSourceId(),
                    getTargetId());
            case MILESTONE -> String.format("%s%s --><> %s", indentation, getSourceId(),
                    getTargetId());
        };
    }


}