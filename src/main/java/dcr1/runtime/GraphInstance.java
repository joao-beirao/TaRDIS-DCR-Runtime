package dcr1.runtime;

//TODO Graph interface

import dcr1.common.DCRGraph;
import dcr1.runtime.elements.events.ComputationEventInstance;
import dcr1.runtime.elements.events.EventInstance;
import dcr1.runtime.elements.events.InputEventInstance;
import dcr1.runtime.elements.events.ReceiveEventInstance;
import dcr1.runtime.elements.relations.ControlFlowRelationInstance;
import dcr1.runtime.elements.relations.SpawnRelationInstance;

/**
 * A mutable object...
 */
public interface GraphInstance
        extends DCRGraph {

    @Override
    Iterable<EventInstance> events();

    @Override
    Iterable<ComputationEventInstance> computationEvents();

    @Override
    Iterable<InputEventInstance> inputEvents();

    @Override
    Iterable<ReceiveEventInstance> receiveEvents();

    @Override
    Iterable<ControlFlowRelationInstance> controlFlowRelations();

    @Override
    Iterable<SpawnRelationInstance> spawnRelations();
}
