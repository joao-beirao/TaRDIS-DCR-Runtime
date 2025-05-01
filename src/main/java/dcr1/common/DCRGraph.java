package dcr1.common;

import dcr1.common.events.ComputationEvent;
import dcr1.common.events.Event;
import dcr1.common.events.InputEvent;
import dcr1.common.events.ReceiveEvent;
import dcr1.common.relations.ControlFlowRelation;
import dcr1.common.relations.SpawnRelation;

public interface DCRGraph {
    Iterable<? extends Event> events();

    Iterable<? extends ComputationEvent> computationEvents();

    Iterable<? extends InputEvent> inputEvents();

    Iterable<? extends ReceiveEvent> receiveEvents();

    Iterable<? extends ControlFlowRelation> controlFlowRelations();

    Iterable<? extends SpawnRelation> spawnRelations();
}
