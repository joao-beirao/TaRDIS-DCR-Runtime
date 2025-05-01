package dcr1.model;

import dcr1.common.data.computation.BooleanExpression;
import dcr1.common.data.computation.ComputationExpression;
import dcr1.common.data.types.Type;
import dcr1.common.events.userset.expressions.UserSetExpression;
import dcr1.common.relations.ControlFlowRelation;
import dcr1.model.events.*;
import dcr1.model.relations.ControlFlowRelationElement;
import dcr1.model.relations.IControlFlowRelationElement;
import dcr1.model.relations.RelationElements;
import dcr1.model.relations.SpawnRelationElement;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

// TODO hide class behind factory builder
public class GraphModelBuilder {

    private final String elementId;
    private final List<Consumer<RecursiveGraphModel>> eventConsumers;
    private final List<Consumer<RecursiveGraphModel>> controlFlowRelationsConsumers;
    private final List<Consumer<RecursiveGraphModel>> spawnGraphConsumers;

    public GraphModelBuilder(String elementId) {
        this.elementId = elementId;
        eventConsumers = new LinkedList<>();
        controlFlowRelationsConsumers = new LinkedList<>();
        spawnGraphConsumers = new LinkedList<>();
    }

    protected String getElementId() {
        return elementId;
    }

    public <T extends Type> GraphModelBuilder addLocalComputationEvent(String elementId,
            String localId, String label, ComputationExpression computation,
            ImmutableMarkingElement initialMarking, BooleanExpression instantiationConstraint, BooleanExpression ifcConstraint ) {
        registerComputationEventElement(
                EventElements.newLocalComputationEvent(elementId, localId, label, computation,
                        initialMarking, instantiationConstraint, ifcConstraint));
        return this;
    }

    public <T extends Type> GraphModelBuilder addComputationEvent(String elementId, String localId,
            String label, ComputationExpression computation, UserSetExpression receivers,
            ImmutableMarkingElement initialMarking, BooleanExpression instantiationConstraint, BooleanExpression ifcConstraint ) {
        registerComputationEventElement(
                EventElements.newComputationEvent(elementId, localId, label, computation,
                        initialMarking, receivers, instantiationConstraint, ifcConstraint));
        return this;
    }

    public <T extends Type> GraphModelBuilder addLocalInputEvent(String elementId, String localId,
            String label, ImmutableMarkingElement initialMarking, BooleanExpression instantiationConstraint, BooleanExpression ifcConstraint ) {
        registerInputEventElement(
                EventElements.newLocalInputEvent(elementId, localId, label, initialMarking,
                        instantiationConstraint, ifcConstraint));
        return this;
    }

    public <T extends Type> GraphModelBuilder addInputEvent(String elementId, String localId,
            String label, UserSetExpression receivers, ImmutableMarkingElement initialMarking,
            BooleanExpression instantiationConstraint, BooleanExpression ifcConstraint ) {
        registerInputEventElement(
                EventElements.newInputEvent(elementId, localId, label, receivers, initialMarking,
                        instantiationConstraint, ifcConstraint));
        return this;
    }

    public <T extends Type> GraphModelBuilder addReceiveEvent(String elementId, String localId,
            String label, UserSetExpression senders, ImmutableMarkingElement initialMarking,
            BooleanExpression instantiationConstraint, BooleanExpression ifcConstraint ) {
        registerReceiveEventElement(
                EventElements.newReceiveEvent(elementId, localId, label, senders, initialMarking,
                        instantiationConstraint, ifcConstraint));
        return this;
    }


    public GraphModelBuilder addControlFlowRelation(String elementId, String srcId, String targetId,
            ControlFlowRelation.Type relationType, BooleanExpression instantiationConstraint) {
        IControlFlowRelationElement element =
                RelationElements.newControlFlowRelation(elementId, srcId, targetId, relationType,
                        instantiationConstraint);
        controlFlowRelationsConsumers.add(graph -> graph.addControlFlowRelation(element));
        return this;
    }

    private void registerControlFlowRelation(ControlFlowRelationElement element) {
        controlFlowRelationsConsumers.add(graph -> graph.addControlFlowRelation(element));
    }

    private <T extends Type> void registerComputationEventElement(
            ComputationEventElement element) {
        eventConsumers.add(graph -> graph.addComputationEvent(element));
    }

    private final <T extends Type> void registerInputEventElement(InputEventElement element) {
        eventConsumers.add(graph -> graph.addInputEvent(element));
    }

    private final <T extends Type> void registerReceiveEventElement(
            ReceiveEventElement element) {
        eventConsumers.add(graph -> graph.addReceiveEvent(element));
    }

    // @Override
    public GraphModelBuilder beginSpawn(String relationElementId, String subgraphElementId,
            String sourceEventId, BooleanExpression instantiationConstraint) {
        return new SpawnGraphModelBuilder(relationElementId, subgraphElementId, sourceEventId,
                instantiationConstraint, this);
    }

    // @Override
    public GraphModelBuilder endSpawn() {
        // TODO [revisit] should probably throw a MalformedGraphException
        return this;
    }

    // @Override
    public RecursiveGraphModel build() {
        return populate(new RecursiveGraphModel(elementId));
    }

    private void addSpawnGraphBuilder(SpawnGraphModelBuilder builder) {
        spawnGraphConsumers.add(builder);
    }

    // recursive "downward" call - each builder passes on its type of graph
    protected RecursiveGraphModel populate(RecursiveGraphModel graph) {
        eventConsumers.forEach(c -> c.accept(graph));
        controlFlowRelationsConsumers.forEach(c -> c.accept(graph));
        spawnGraphConsumers.forEach(c -> c.accept(graph));
        return graph;
    }

    /**
     * The builder extends the {@link GraphModelBuilder} by keeping track of the parent scope, as
     * well as of the event that triggers the spawn.
     */
    private static final class SpawnGraphModelBuilder
            extends GraphModelBuilder
            implements Consumer<RecursiveGraphModel> {

        private final String sourceEventId;
        private final String subgraphElementId;
        private final BooleanExpression instantiationConstraint;
        private final GraphModelBuilder outerScope;

        private SpawnGraphModelBuilder(String relationElementId, String subgraphElementId,
                String sourceEventId, BooleanExpression instantiationConstraint, GraphModelBuilder outerScope) {
            super(relationElementId);
            this.sourceEventId = sourceEventId;
            this.instantiationConstraint = instantiationConstraint;
            this.outerScope = outerScope;
            this.subgraphElementId = subgraphElementId;
        }

        @Override
        public GraphModelBuilder endSpawn() {
            outerScope.addSpawnGraphBuilder(this);
            return outerScope;
        }

        //  just route the call to the top level
        @Override
        public RecursiveGraphModel build() {
            return outerScope.build();
        }

        @Override
        public void accept(RecursiveGraphModel model) {
            RecursiveGraphModel subgraph = populate(new RecursiveGraphModel(subgraphElementId));
            SpawnRelationElement spawnElement =
                    new SpawnRelationElement(getElementId(), sourceEventId, subgraph,
                            instantiationConstraint);
            model.addSpawnRelation(spawnElement);
        }
    }
}
