package dcr1.runtime;

import dcr1.common.data.types.Type;
import dcr1.runtime.events.ComputationEventInstance;
import dcr1.runtime.events.EventInstance;
import dcr1.runtime.events.InputEventInstance;
import dcr1.runtime.events.ReceiveEventInstance;
import dcr1.runtime.relations.ControlFlowRelationInstance;
import dcr1.runtime.relations.SpawnRelationInstance;

import java.util.*;
import java.util.stream.Collectors;

final class GraphState
        implements GraphInstance {

  private final Map<String, EventInstance<? extends Type>> eventsById;
  private final Map<String, ComputationEventInstance<? extends Type>> computationEvents;

  // ==
  // convenience mappings for efficiency reasons
  // -> (outgoing direction) relations for which the execution of the 'source'
  // event may affect the state of the 'target' event - indexed by 'source' event
  private final Map<EventInstance<?>, List<EventInstance<?>>> includes;
  private final Map<EventInstance<?>, List<EventInstance<?>>> excludes;
  private final Map<EventInstance<?>, List<EventInstance<?>>> responses;
  // -> (incoming direction) - relations for which the 'source' event can actively
  // restrict the execution of the 'target' event - indexed by 'target' event
  private final Map<EventInstance<?>, List<EventInstance<?>>> conditions;
  private final Map<EventInstance<?>, List<EventInstance<?>>> milestones;
  // event ids mapped to spawn models
  private final Map<String, List<SpawnRelationInstance>> spawnRelations;


  GraphState() {
    this.eventsById = new HashMap<>();
    //
    this.computationEvents = new HashMap<>();
    //
    includes = new HashMap<>();
    excludes = new HashMap<>();
    responses = new HashMap<>();
    conditions = new HashMap<>();
    milestones = new HashMap<>();
    spawnRelations = new HashMap<>();
  }

  @Override
  public Iterable<EventInstance<?>> events() {
    return eventsById.values();
    // TODO not yet implemented
    // throw new RuntimeException("Not yet implemented");
  }

  @Override
  public Iterable<ComputationEventInstance<? extends Type>> computationEvents() {
    return computationEvents.values();
  }

  @Override
  public Iterable<InputEventInstance<?>> inputEvents() {
    // TODO not yet implemented
    throw new RuntimeException("Not yet implemented");
  }

  @Override
  public Iterable<ReceiveEventInstance<?>> receiveEvents() {
    // TODO not yet implemented
    throw new RuntimeException("Not yet implemented");
  }

  @Override
  public Iterable<ControlFlowRelationInstance> controlFlowRelations() {
    // TODO not yet implemented
    throw new RuntimeException("Not yet implemented");
  }

  Iterable<EventInstance<?>> includes(String eventId) {
    return includes.getOrDefault(eventsById.get(eventId), Collections.emptyList());
  }

  Iterable<EventInstance<?>> excludes(String eventId) {
    return excludes.getOrDefault(eventsById.get(eventId), Collections.emptyList());
  }

  Iterable<EventInstance<?>> responses(String eventId) {
    return responses.getOrDefault(eventsById.get(eventId), Collections.emptyList());
  }

  @Override
  public Iterable<SpawnRelationInstance> spawnRelations() {
    // TODO not yet implemented
    throw new RuntimeException("Not yet implemented");
  }


  // TODO [extend implementation] include guards?
  boolean isEnabled(EventInstance<?> event) {
    // if (!event.getInitiator().equals(projectionRole)) {
    //     return false;
    // }
    //
    if (!event.marking().isIncluded()) {
      return false;
    }
    for (EventInstance<?> e : conditions.getOrDefault(event, Collections.emptyList())) {
      if (e.marking().isIncluded() && !e.marking().hasExecuted()) {
        return false;
      }
    }
    for (EventInstance<?> e : milestones.getOrDefault(event, Collections.emptyList())) {
      if (e.marking().isIncluded() && e.marking().isPending()) {
        return false;
      }
    }

    return true;
  }

  Iterable<SpawnRelationInstance> spawnRelations(String sourceId) {
    return spawnRelations.getOrDefault(sourceId, Collections.emptyList());
  }

  Optional<EventInstance<?>> getEventById(String eventId) {
    return Optional.ofNullable(eventsById.get(eventId));
  }

  void addComputationEvent(ComputationEventInstance<? extends Type> event) {
    eventsById.put(event.getGlobalId(), event);
    computationEvents.put(event.getGlobalId(), event);
  }

  void addInputEvent(InputEventInstance<? extends Type> event) {
    eventsById.put(event.getGlobalId(), event);
    // TODO [implement for real]
    // throw new RuntimeException("TODO add to input events");
  }

  void addReceiveEvent(ReceiveEventInstance<? extends Type> event) {
    eventsById.put(event.getGlobalId(), event);
    throw new RuntimeException("TODO add to receive events");
  }

  private void addControlFlowRelation(EventInstance<?> key, EventInstance<?> newVal,
      Map<EventInstance<?>, List<EventInstance<?>>> mapping) {
    List<EventInstance<?>> entries = mapping.getOrDefault(key, new LinkedList<>());
    if (entries.isEmpty()) {
      mapping.put(key, entries);
    }
    entries.add(newVal);
  }

  void addControlFlowRelation(ControlFlowRelationInstance relation) {
    EventInstance<?> src = eventsById.get(relation.getSourceId());
    EventInstance<?> tgt = eventsById.get(relation.getTargetId());
    switch (relation.getRelationType()) {
      case INCLUDE -> addControlFlowRelation(src, tgt, includes);
      case EXCLUDE -> addControlFlowRelation(src, tgt, excludes);
      case RESPONSE -> addControlFlowRelation(src, tgt, responses);
      case CONDITION -> addControlFlowRelation(tgt, src, conditions);
      case MILESTONE -> addControlFlowRelation(tgt, src, milestones);
    }
  }

  // void addSpawnRelation(String sourceUID, SpawnRelation spawnRelation) {
  //     List<SpawnRelation> spawns = spawnRelations.getOrDefault(sourceUID,
  //             new LinkedList<>());
  //     spawns.add(spawnRelation);
  //     spawnRelations.putIfAbsent(sourceUID, spawns);
  // }

  void resetState() {
    // TODO not yet implemented
    throw new RuntimeException();
  }

  @Override
  public String toString() {
    return "Graph State:\n" + " = Events =\n" + computationEvents
        .values()
        .stream()
        .map(ComputationEventInstance::toString)
        .collect(Collectors.joining("\n"));
  }


}
