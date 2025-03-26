package dcr1.model;


import dcr1.common.Environment;
import dcr1.common.events.InputEvent;
import dcr1.common.events.ReceiveEvent;
import dcr1.common.data.types.Type;
import dcr1.model.events.ComputationEventElement;
import dcr1.model.events.EventElement;
import dcr1.model.events.InputEventElement;
import dcr1.model.events.ReceiveEventElement;
import dcr1.model.relations.IControlFlowRelationElement;
import dcr1.model.relations.ISpawnRelationElement;
import dcr1.model.relations.SpawnRelationElement;

import java.util.*;
import java.util.function.Function;

public final class RecursiveGraphModel
        extends GenericElement
        implements GraphModel {

    private final Map<String, EventElement<? extends Type>> eventsByLocalId;

    // TODO [revisit] possibly discard computationEvents
    private final Map<String, ComputationEventElement<? extends Type>> computationEvents;
    private final Set<IControlFlowRelationElement> controlFlowRelations;

    // TODO should eventually be a list of relations per event
    private final List<ISpawnRelationElement> spawnRelations;

    RecursiveGraphModel(String elementId) {
        super(elementId);
        this.eventsByLocalId = new HashMap<>();
        this.computationEvents = new HashMap<>();
        // TODO [revise] use of HashSet with ControlFlowRelationElements
        this.controlFlowRelations = new HashSet<>();
        this.spawnRelations = new LinkedList<>();
    }


    @Override
    public Iterable<? extends EventElement<? extends Type>> events() {
        return eventsByLocalId.values();
    }

    @Override
    public Iterable<ComputationEventElement<? extends Type>> computationEvents() {
        return computationEvents.values();
    }

    @Override
    public Iterable<IControlFlowRelationElement> controlFlowRelations() {
        return controlFlowRelations;
    }

    @Override
    public Iterable<? extends InputEvent<?>> inputEvents() {
        // TODO [implement]
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public Iterable<? extends ReceiveEvent<?>> receiveEvents() {
        return null;
    }

    @Override
    public Iterable<ISpawnRelationElement> spawnRelations() {
        return spawnRelations;
    }

    // TODO [revisit] code defensively ? Likely difficult and redundant since the compiler will have
    //  gone through this
    // String label, ASTNode expression,
    // Type type, EventMarking marking
    void addComputationEvent(ComputationEventElement<? extends Type> event) {
        // TODO uncomment
        eventsByLocalId.putIfAbsent(event.localId(), event);
        computationEvents.putIfAbsent(event.localId(), event);
    }

    void addInputEvent(InputEventElement<? extends Type> event) {
        eventsByLocalId.putIfAbsent(event.localId(), event);
    }
    void addReceiveEvent(ReceiveEventElement<? extends Type> event) {
        eventsByLocalId.putIfAbsent(event.localId(), event);
    }



    void addControlFlowRelation(IControlFlowRelationElement relationElement) {
        // TODO
        controlFlowRelations.add(relationElement);
    }

    void addSpawnRelation(SpawnRelationElement relationElement) {
        spawnRelations.add(relationElement);
    }

    private static Function<ModelElement, String> stringifier = ModelElement::unparse;

    private static String beginSpawn(String indent, String sourceId) {
        return String.format("\n%s%s -->> {", indent, sourceId);
    }

    private static String endSpawn(String indent) {
        return String.format("\n%s}", indent);
    }


    private static String tester(RecursiveGraphModel graph, String indent,
            Function<ModelElement, String> stringifier) {
        StringBuilder builder = new StringBuilder();
        builder.append(System.lineSeparator())
                .append(indent)
                .append("(<")
                .append(graph.getElementId())
                        .append(">)");
        graph.eventsByLocalId.values()
                .forEach(event -> builder.append(System.lineSeparator())
                        .append(indent)
                        .append(stringifier.apply(event)));
        if (!graph.spawnRelations.isEmpty()) {
            builder.append(System.lineSeparator()).append(indent).append(";");
        }
        graph.spawnRelations.forEach(spawn -> {
            builder.append(beginSpawn(indent, spawn.getSourceId()));
            builder.append(
                    tester((RecursiveGraphModel) spawn.getSubgraph(), indent + "  ", stringifier));
            builder.append(endSpawn(indent));
        });
        return builder.toString();
    }

    @Override
    public String toString() {
        return new StringBuilder().append("{")
                .append(tester(this, "  ", Objects::toString))
                .append(System.lineSeparator())
                .append("}").toString();
    }

    public String toString(String indent) {
        return tester(this, indent, Objects::toString);
    }

    @Override
    public String unparse() {return tester(this, "", ModelElement::unparse);}

    /**
     * Gathers both static and dynamic info required to instantiate a DCRGraph ... (subgraph?)
     */
    private static final class Spawn {

        // static information
        final RecursiveGraphModel graph;
        // dynamic info
        final String trigger;
        final Environment<String> names;

        Spawn(RecursiveGraphModel graph, String trigger, Environment<String> names) {
            this.graph = graph;
            this.trigger = trigger;
            this.names = names;
        }
    }
}
