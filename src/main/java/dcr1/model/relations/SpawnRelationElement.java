package dcr1.model.relations;

import dcr1.common.data.computation.BooleanExpression;
import dcr1.model.GraphModel;
import dcr1.model.RecursiveGraphModel;

public final class SpawnRelationElement
        extends GenericRelationElement
        implements ISpawnRelationElement {

    private final GraphModel subgraph;

    public SpawnRelationElement(String elementId, String sourceEventId,
            RecursiveGraphModel subgraph) {
        super(elementId, sourceEventId);
        this.subgraph = subgraph;
    }

    public SpawnRelationElement(String elementId, String sourceEventId, BooleanExpression guard,
            RecursiveGraphModel subgraph) {
        super(elementId, sourceEventId, guard);
        this.subgraph = subgraph;
    }

    @Override
    public GraphModel getSubgraph() {
        return subgraph;
    }


    @Override
    public String toString() {
        // TODO
        return "SpawnRelationElement";
        // return String.format("<elemId: %s> %s -->> \n{ %s", getElementId(), getSourceId(), ,
        //         getTargetId());
    }


    @Override
    public String unparse() {
        return null;
    }
}

