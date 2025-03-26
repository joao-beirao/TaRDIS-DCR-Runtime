package dcr1.runtime;

//TODO Graph interface

import dcr1.common.data.types.Type;
import dcr1.common.DCRGraph;
import dcr1.runtime.events.ComputationEventInstance;
import dcr1.runtime.events.EventInstance;
import dcr1.runtime.events.InputEventInstance;
import dcr1.runtime.events.ReceiveEventInstance;
import dcr1.runtime.relations.ControlFlowRelationInstance;
import dcr1.runtime.relations.SpawnRelationInstance;

/**
 * A mutable object...
 */
public interface GraphInstance
        extends DCRGraph {

    @Override
    Iterable<EventInstance<?>> events();

    @Override
    Iterable<ComputationEventInstance<? extends Type>> computationEvents();

    @Override
    Iterable<InputEventInstance<?>> inputEvents();

    @Override
    Iterable<ReceiveEventInstance<?>> receiveEvents();

    @Override
    Iterable<ControlFlowRelationInstance> controlFlowRelations();

    @Override
    Iterable<SpawnRelationInstance> spawnRelations();
}
