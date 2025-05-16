package deprecated.dcr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import deprecated.dcr.Relation.Type;
import deprecated.dcr.ast.ASTNode;
import deprecated.dcr.ast.DynamicTypeCheckException;
import deprecated.dcr.ast.Environment;
import deprecated.dcr.ast.UndeclaredIdentifierException;
import deprecated.dcr.ast.values.RecordVal;
import deprecated.dcr.ast.values.RefVal;
import deprecated.dcr.ast.values.Value;

/**
 * === DCR Graph encoding ===
 * [static part] (1) nodes and (2) relations, reflecting the directed graph
 * [dynamic part] (3) a marking, reflecting the runtime state of the graph
 *
 * (1) graph nodes (seen as executable events)
 * // (2) (directed) relations between nodes (source event -> target event)
 */
public final class DCRGraphClass implements DCRGraph {

  // ========================================================================
  // ===================================================== INSTANCE VARIABLES
  // ========================================================================

  // the role associated with this endpoint projection
  private final String projectionRole;
  // nodes mapped by id
  private final Map<String, DcrEvent> eventsById;
  // relations between nodes
  private final Set<Relation> relations;
  // convenience mappings for listing events by type
  private final Map<DcrEvent, InputAction> inputActions;
  private final Map<DcrEvent, ComputationAction> computationActions;
  private final Map<DcrEvent, ComputationSendOperation> sendOperations;
  private final Map<DcrEvent, ReceiveOperation> receiveOperations;
  // convenience mappings for efficiency reasons
  // -> (outgoing direction) relations for which the execution of the 'source'
  // event may affect the state of the 'target' event - indexed by 'source' event
  private final Map<DcrEvent, List<DcrEvent>> include;
  private final Map<DcrEvent, List<DcrEvent>> exclude;
  private final Map<DcrEvent, List<DcrEvent>> response;
  // -> (incoming direction) - relations for which the 'source' event can actively
  // restrict the execution of the 'target' event - indexed by 'target' event
  private final Map<DcrEvent, List<DcrEvent>> condition;
  private final Map<DcrEvent, List<DcrEvent>> milestone;
  // enviroment
  private final Environment env;
  // private final Map<String, SpawnGenerator>

  // ========================================================================
  // =========================================================== CONSTRUCTORS
  // ========================================================================

  // private construtor - class can only be instantiated via builder
  private DCRGraphClass(String projectionRole, Environment env) {
    this.projectionRole = projectionRole;
    this.eventsById = new HashMap<>();
    this.relations = new HashSet<>();
    this.inputActions = new HashMap<>();
    this.computationActions = new HashMap<>();
    this.sendOperations = new HashMap<>();
    this.receiveOperations = new HashMap<>();
    this.include = new HashMap<>();
    this.exclude = new HashMap<>();
    this.response = new HashMap<>();
    this.condition = new HashMap<>();
    this.milestone = new HashMap<>();
    this.env = env;
  }

  // ========================================================================
  // ========================================================= PUBLIC METHODS
  // ========================================================================

  // ------------------------------------------------------------------------
  // ---------------------------------------------------------- graph builder
  //

  /**
   * Create a new DCR graph builder for a given role and environment to build a
   * DCR graph.
   *
   * @param role of the initiator of the DCR graph projection
   * @return A new DCR graph builder instance
   */
  public static final DCRGraphBuilder newBuilder(String role) {
    return new DCRGraphBuilder(role);
  }

  // ------------------------------------------------------------------------
  // ------------------------------------------------------- view graph state
  //

  /**
   * Retrieve an event by id.
   *
   * @param eventId the id of the event to retrieve.
   * @return An optional containing the event with the supplied if; and empty
   *         optional otherwise.
   */
  public Optional<Event> getEvent(String eventId) {
    return Optional.ofNullable(eventsById.get(eventId));
  }

  @Override
  public Iterable<Event> getEvents() {
    return new ArrayList<>(this.eventsById.values());
  }

  public Iterable<Event> getEnabledEvents() {
    return eventsById.values()
        .stream()
        .filter((e) -> isEnabled(e))
        .collect(Collectors.toList());
  }

  // Note: Not returning a (an immutable) snapshot
  @Override
  public Iterable<InputAction> getInputActions() {
    return this.inputActions.values();
  }

  @Override
  public Iterable<ComputationAction> getComputationActions() {
    return this.computationActions.values();
  }

  @Override
  public Iterable<ComputationSendOperation> getSendOperations() {
    return this.sendOperations.values();
  }

  @Override
  public Iterable<ReceiveOperation> getReceiveOperations() {
    return this.receiveOperations.values();
  }

  @Override
  public Iterable<Relation> getRelations() {
    return this.relations;
  }

  //
  // ------------------------------------------------------------------------
  // -------------------------------------------------------- EVENT EXECUTION
  //

  private static void bindEvent(String eventId, Value initialValue, Environment env) {
    // TODO [revisit] move constant vals to something static?
    env.bind(eventId, RecordVal.builder().addField("value", new RefVal(initialValue)).build());
  }

  private static Value getEventValue(Value lookupVal) {
    // TODO [revisit] move constant vals to something static?
    return ((RefVal) (((RecordVal) lookupVal).getValue("value")));
  }

  private static void setEventValue(String eventId, Value newValue, Environment env)
      throws UndeclaredIdentifierException {
    // System.err.println("lookup val before " +lookupValue );
    // System.err.println("newVal before " +newValue );
    // TODO [revisit] move constant vals to something static?
    ((RefVal) (((RecordVal) env.lookup(eventId)).getValue("value"))).setValue(newValue);
    // System.err.println("lookup val after " +lookupValue );
  }

  /*
   * Attempt to execute an event for which this endpoint's role is the
   * initiator. It is the caller's responsability to ensure the event is
   * enabled.
   *
   * This method is intended to be called for each a specialized 'execute'
   * event, whenever the initiator matches this endpoint's role (which currently
   * just excludes ReceiveOperation events)
   */
  private ExecutionResult onEventExecute(DcrEvent event, Value newValue) {
    ExecutionResult result = new ExecutionResult();
    // make the event executed and not pending before propagating relation effects
    // to prevent effects of self-targeting relations from being hidden
    event
        .setExecuted(true)
        .setPending(false)
        .setValue(newValue);
    // propagate effects of state-changing relations
    propagateRelationBasedEffects(event, result);
    // just add the event as modified without checking (for simplicity sake)
    result.addModifiedEvents(event);
    // note: getMarking() returns
    result.setMarking(event.getMarking());
    return result;
  }

  private ExecutionResult onComputationEventExecute(DcrComputationEvent event)
      throws UndeclaredIdentifierException, DynamicTypeCheckException {
    // recompute value
    Value recomputedValue = event.getComputationExpression().eval(env);
    System.err.println("recomputed Val instanceof: " + recomputedValue.getClass().getSimpleName());
    // update both environment and event with freshly recomputed value
    setEventValue(event.getId(), recomputedValue, env);
    // ((EventVal) env.lookup(event.getId())).setValue(recomputedValue);
    return onEventExecute(event, recomputedValue);
  }

  private ExecutionResult executeInputEvent(DcrInputEvent event, Value inputValue)
      throws UndeclaredIdentifierException {

    // ((RefVal)(((RecordVal)
    // env.lookup(event.getId())).getField("value")).getValue()).setValue(inputValue);
    setEventValue(event.getId(), inputValue, env);

    return onEventExecute(event, inputValue);
  }

  // Executes the (local) InputAction identified by eventId;
  // Projectability ensures that a local action does not influence events
  // of other initiators;
  // Executing an InputAction will amount to store the input value;
  /**
   *
   * @param eventId
   * @param inputValue
   * @return
   * @throws UndeclaredIdentifierException
   */
  public ExecutionResult executeInputAction(String eventId, Value inputValue) throws UndeclaredIdentifierException {
    // TODO throw custom exception
    Objects.requireNonNull(eventId);
    Objects.requireNonNull(inputValue);

    InputAction event = getEventById(eventId, InputAction.class)
        .orElseThrow(IllegalArgumentException::new);

    throwOnNotEnabledEvent(event);
    // if we are here, we found the InputAction and it's enabled

    if (!event.getTypeExpression().equals(inputValue.getType())) {
      throw new IllegalArgumentException("wrong type of val");
    }

    return executeInputEvent(event, inputValue);
  }

  /**
   *
   * @param eventId
   * @return
   * @throws UndeclaredIdentifierExceptionxs
   * @throws DynamicTypeCheckException
   */
  public ExecutionResult executeComputationAction(String eventId)
      throws DynamicTypeCheckException, UndeclaredIdentifierException {
    Objects.requireNonNull(eventId);
    // TODO throw custom exception
    ComputationAction event = getEventById(eventId, ComputationAction.class)
        .orElseThrow(IllegalArgumentException::new);
    throwOnNotEnabledEvent(event);
    // if we are here, we found the ComputationAction and it's enabled

    return onComputationEventExecute(event);
  }

  /**
   *
   * @param eventId
   * @return
   * @throws UndeclaredIdentifierException
   * @throws DynamicTypeCheckException
   */
  public ExecutionResult executeComputationSendOperation(String eventId)
      throws DynamicTypeCheckException, UndeclaredIdentifierException {
    // TODO throw custom exception
    Objects.requireNonNull(eventId);
    ComputationSendOperation event = getEventById(eventId, ComputationSendOperation.class)
        .orElseThrow(IllegalArgumentException::new);
    throwOnNotEnabledEvent(event);
    // if we are here, we found the SendOperation and it's enabled

    ExecutionResult result = onComputationEventExecute(event);
    result.addAllReceivers(event.getReceivers());
    return result;
  }

  public ExecutionResult executeInputSendOperation(String eventId, Value inputValue)
      throws DynamicTypeCheckException, UndeclaredIdentifierException {
    // TODO throw custom exception
    Objects.requireNonNull(eventId);
    InputSendOperation event = getEventById(eventId, InputSendOperation.class)
        .orElseThrow(IllegalArgumentException::new);
    throwOnNotEnabledEvent(event);
    // if we are here, we found the SendOperation and it's enabled

    // if (!event.getTypeExpression().equals(inputValue.getType())) {
    //   throw new IllegalArgumentException("wrong type of val");
    // }
    if (!event.getTypeExpression().equalsType(inputValue.getType())) {
      throw new IllegalArgumentException("wrong type of val");
    }

    ExecutionResult result = executeInputEvent(event, inputValue);;
    result.addAllReceivers(event.getReceivers());
    return result;
  }

  // Reminder: this is not a 'true' Execute - this is replicating the actual
  // execution on the sender-side (the real initiator)
  /**
   *
   * @param eventId
   * @param marking
   * @return
   * @throws UndeclaredIdentifierException
   */
  public ExecutionResult executeReceiveOperation(String eventId, EventMarking marking)
      throws UndeclaredIdentifierException {
    // TODO throw custom exception
    Objects.requireNonNull(eventId);
    Objects.requireNonNull(marking);
    // TODO [assess] marking shouldn't also have a Unit val at this point

    ReceiveOperation event = getEventById(eventId, ReceiveOperation.class)
        .orElseThrow(IllegalArgumentException::new);
    // if we are here, we found the SendOperation

    // udpdate environment to reflect the value enclosed within the new marking
    setEventValue(event.getId(), marking.getValue(), env);
    // ((EventVal) env.lookup(eventId)).setValue(marking.getValue());
    // udpdate event to reflect the received marking
    event.setMarking(marking);

    ExecutionResult result = new ExecutionResult();
    // just assume the event has been modified (for simplicity sake)
    result.addModifiedEvents(event);
    // propagate any side effects on our end
    propagateRelationBasedEffects(event, result);
    result.setMarking(event.getMarking());
    return result;
  }

  @Override
  public String toString() {
    // TODO [extend] reflect relations - currently only events
    return eventsById
        .entrySet()
        .stream()
        .sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
        .map((e) -> e.getValue().toString())
        .collect(Collectors.joining("\n"));
  }

  public String unparse() {
    return eventsById
        .entrySet()
        .stream()
        .sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
        .map((e) -> e.getValue().unparse())
        .collect(Collectors.joining("\n"));
  }

  //
  // ========================================================================
  // ======================================================== PRIVATE METHODS
  // ========================================================================

  // ------------------------------------------------------------------------
  // -------------------------------------------- adding events and relations
  //

  // ancillary - called by the specialized event-adding methods
  private DCRGraphClass addEvent(DcrEvent e, Environment env) {
    eventsById.put(e.getId(), e);
    // add a mapping for uniform handling later on (might remain empty)
    include.put(e, new LinkedList<>());
    exclude.put(e, new LinkedList<>());
    response.put(e, new LinkedList<>());
    condition.put(e, new LinkedList<>());
    milestone.put(e, new LinkedList<>());
    // we bind events to {"value":ValRef} for uniformity, proximity with
    // the "event.value.field" deref-style present in ReGraDa and extensibility (not
    // really sure whether we'll be using some other property of an event - e.g.,
    // marking?)
    // Value val = RecordVal
    // .newBuilder()
    // .addField("value", new RefVal(e.getMarking().getValue()))
    // .build();
    bindEvent(e.getId(), e.getMarking().getValue(), env);
    // env.bind(e.getId(), val);
    return this;
  }

  // add an input action to this endoint
  private DCRGraph addInputAction(String eventId, String action, String initiator, deprecated.dcr.ast.typing.Type type, EventMarking marking) {
    InputAction event = new InputAction(eventId, action, initiator, type, marking);
    this.inputActions.put(event, event);
    return this.addEvent(event, env);
  }

  // add a computation action to this endpoint
  private DCRGraph addComputationAction(String eventId, String action, String initiator, ASTNode expression,
                                        deprecated.dcr.ast.typing.Type type, EventMarking marking) {
    ComputationAction event = new ComputationAction(eventId, action, initiator, expression, type, marking);
    this.computationActions.put(event, event);
    return this.addEvent(event, env);
  }

  // add a send operation (sender-perspective of an interaction) to this endpoint
  private DCRGraph addComputationSendOperation(String eventId, String action, String initiator, Set<String> receivers,
                                               ASTNode expression, deprecated.dcr.ast.typing.Type type, EventMarking marking) {
    DcrEvent sendOperation = new ComputationSendOperation(eventId, action, initiator, receivers, expression, type,
        marking);
    return this.addEvent(sendOperation, env);
  }

  // add a send operation (sender-perspective of an interaction) to this endpoint
  private DCRGraph addInputSendOperation(String eventId, String action, String initiator, Set<String> receivers,
                                         deprecated.dcr.ast.typing.Type typeExpression, EventMarking marking) {
    DcrEvent inputSendOperation = new InputSendOperation(eventId, action, initiator, receivers, typeExpression, marking);
    return this.addEvent(inputSendOperation, env);
  }

  // add a receive operation (receiver-perspective of an interaction) to this
  // endpoint
  private DCRGraph addReceiveOperation(String eventId, String action, String initiator, String receiver,
                                       deprecated.dcr.ast.typing.Type type, EventMarking marking) {
    DcrEvent receiveOperation = new ReceiveOperation(eventId, action, initiator, receiver, type, marking);
    return this.addEvent(receiveOperation, env);
  }

  // add a relation to this endpoint
  private DCRGraph addRelation(Type relation, String sourceEventId, String targetEventId) {
    DcrEvent from = eventsById.get(sourceEventId);
    DcrEvent to = eventsById.get(targetEventId);
    relations.add(new Relation(relation, from, to));
    switch (relation) {
      case INCLUDE:
        include.get(from).add(to);
        break;
      case EXCLUDE:
        exclude.get(from).add(to);
        break;
      case RESPONSE:
        response.get(from).add(to);
        break;
      case CONDITION:
        condition.get(to).add(from);
        break;
      case MILESTONE:
        milestone.get(to).add(from);
        break;
      default:
        break;
    }
    return this;
  }

  // ------------------------------------------------------------------------
  // ------------------------------------------------------- executing events
  //

  private void throwOnNotEnabledEvent(Event event) {
    if (!this.isEnabled(event)) {// TODO throw custom exception instead
      throw new IllegalArgumentException("Event is not enabled");
    }
  }

  private boolean isEnabled(Event event) {
    // only if I'm the initiator

    if (!event.getInitiator().equals(projectionRole)) {
      return false;
    }

    // event is not included
    if (!event.getMarking().isIncluded()) {
      return false;
    }

    // conditionEvent ->* event
    for (Event e : condition.get(((DcrEvent) event))) {
      if (e.getMarking().isIncluded() && !e.getMarking().isExecuted()) {
        return false;
      }
    }
    // milestoneEvent -><> event
    for (Event e : milestone.get(event)) {
      if (e.getMarking().isIncluded() && e.getMarking().isPending()) {
        return false;
      }
    }

    return true;
  }

  /*
   * Enforce state changes derived from relations upon event execution; We assume
   * targeted events effectively change and register them as modified without
   * checking (for simplicity sake)
   */
  private void propagateRelationBasedEffects(Event event, ExecutionResult result) {
    // TODO will eventually test relation guards here when they are added
    // TODO will eventually process spawn relations here when they are added

    // response relations: some events might get pending
    response.get(event).forEach((e) -> {
      if (!e.getMarking().isPending()) {
        result.addModifiedEvents(e);
      }
      e.setPending(true);
    });
    // exclude relations: some events might get excluded
    exclude.get(event).forEach((e) -> {
      if (e.getMarking().isIncluded())
        result.addModifiedEvents(e);
      e.setIncluded(false);
    });
    // include relations: some events might get included
    include.get(event).forEach((e) -> {
      if (!e.getMarking().isIncluded())
        result.addModifiedEvents(e);
      e.setIncluded(true);
    });
  }

  // ------------------------------------------------------------------------
  // -------------------------------------------------------------- ancillary
  //

  /*
   * Ancilary method used to retrieve events of a specific type.
   *
   * Returns an Optional<T> containing the event of class T if such event exists,
   * and an empty Optional otherwise
   */
  private <T extends DcrEvent> Optional<T> getEventById(String eventId, Class<T> clazz) {
    return Optional
        .ofNullable(eventsById.get(eventId))
        .map((e) -> e.getClass().equals(clazz) ? clazz.cast(e) : null);
  }

  /**
   * A builder for constructing a {@link DCRGraph DCR Graph}.
   *
   * This class offers convenience methods for setting up a
   * {@link DCRGraph DCR Graph}, namely, to define its events, and the relations
   * between them.
   * </p>
   *
   * The {@link DCRGraphBuilder#build() build()} method returns a new DCR graph
   * instance reflecting the events and relations previously
   * added to (and buffered by) this builder. Each call to
   * {@link DCRGraphBuilder#build() build()}
   * generates a new DCR graph instance, allowing for additional events and
   * relations can be added in-between calls. Changes in one instance do not
   * affect other instances.
   * </p>
   *
   * Events in a graph must be assigned unique identifiers. Therefore, attempting
   * to reuse an id when adding events will result in an error.
   * </p>
   *
   * This builder acts as a buffer, enabling events and relations to be added
   * in any order in between {@link DCRGraphBuilder#build() build()} calls.
   * However, when {@link DCRGraphBuilder#build() build()} is called, every event
   * referenced in a relation must have also been added. Failing to do so will
   * raise an error.
   *
   */
  public static final class DCRGraphBuilder {

    private final List<Consumer<DCRGraphClass>> events;
    private final List<Consumer<DCRGraphClass>> relations;
    private final Set<String> usedIds;
    private final Set<String> relationEventIds;
    private final String role;
    DCRGraphBuilder parent;
    // private final Map<String, DCRGraphBuilder> graphBySpawnEvent;

    DCRGraphBuilder(String role) {
      this.events = new LinkedList<>();
      this.relations = new LinkedList<>();
      this.usedIds = new HashSet<>();
      this.relationEventIds = new HashSet<>();
      this.role = role;
      this.parent = null;
      // this.graphBySpawnEvent = new HashMap<>();
    }

    // TODO: implement spawn
    DCRGraphClass buildSpawn(DCRGraphClass graph) {
      graph.env.beginScope();
      return null;
    }

    public DCRGraphClass build() {
      if (relationEventIds.stream().filter(e -> !usedIds.contains(e)).findAny().isPresent()) {
        // TODO / FIXME not an illegal argument - use custom exception instead
        throw new IllegalArgumentException("Illegal Graph: relations reference events not in this graph");
      }
      DCRGraphClass graph = new DCRGraphClass(role, new Environment());
      // TODO spawns will complicate things here - we will have sort out scopes here
      // and inserted in order, adequately calling beginScope in-between
      // add all events first, and all relations after
      events.forEach((c) -> c.accept(graph));
      relations.forEach((c) -> c.accept(graph));
      return graph;
    }

    private static final String nullArgErrorMsgOf(String argName) {
      return String.format("'%s' : cannot be null.", argName);
    }

    private static <T> T requireArgNonNull(T arg, String argName) {
      return Objects.requireNonNull(arg, nullArgErrorMsgOf(argName));
    }

    private static final String errorMsgBlankStringArg(String argName) {
      return String.format("'%s' : cannot be a blank String", argName);
    }

    private static final String sanitizeStringArg(String arg, String argName) {
      if (requireArgNonNull(arg, argName).isBlank()) {
        throw new IllegalArgumentException(errorMsgBlankStringArg(argName));
      }
      return arg.trim();
    }

    private static final String sanitizeEventId(String eventId) {
      String argName = "eventId";
      String arg = sanitizeStringArg(eventId, argName);
      return arg;
    }

    private final String errorMsgUniqueEventIds(String argName, String argVal) {
      return String.format("'%s' = '%s' : event identifiers must be unique", argName, argVal);
    }

    private final String errorMsgInitiatorAsReceiver(String argVal) {
      return String.format(
          "%s : cannot declare participant as both an initiator and a receiver of a interaction.",
          argVal);
    }

    private String sanitizeAndRegisterEventId(String eventId) {
      String sanitizedId = sanitizeEventId(eventId);
      if (!usedIds.add(sanitizedId))
        throw new IllegalArgumentException(errorMsgUniqueEventIds("eventId", eventId));
      return sanitizedId;
    }

    private String sanitizeAndRegisterRelationEventId(String eventId) {
      String sanitizedId = sanitizeEventId(eventId);
      relationEventIds.add(sanitizedId);
      return sanitizedId;
    }

    private String sanitizeAction(String action) {
      return sanitizeStringArg(action, "action");
    }

    private String sanitizeInitiator(String role) {
      return sanitizeStringArg(role, "initiator role");
    }

    private EventMarking sanitizeMarking(EventMarking eventMarking) {
      return requireArgNonNull(eventMarking, "eventMarking");
    }

    // assumes initiator was already sanitized
    private String sanitizeReceiver(String sanitizeInitiator, String receiver) {
      String rcvr = sanitizeStringArg(receiver, "receiver role");
      if (sanitizeInitiator.equalsIgnoreCase(rcvr))
        throw new IllegalArgumentException(errorMsgInitiatorAsReceiver(rcvr));
      return rcvr;
    }

    private Set<String> sanitizeReceivers(String initiator, Set<String> receivers) {
      return receivers
          .stream()
          .map(((e) -> sanitizeReceiver(initiator, e)))
          .collect(Collectors.toCollection(HashSet::new));
    }

    private Type sanitizeRelationType(Type type) {
      return requireArgNonNull(type, "relation type");
    }

    public DCRGraphBuilder addInputAction(String eventId, String action, String initiator, deprecated.dcr.ast.typing.Type type,
        EventMarking eventMarking) {
      events.add((graph) -> graph.addInputAction(
          sanitizeAndRegisterEventId(eventId),
          sanitizeAction(action),
          sanitizeInitiator(initiator),
          Objects.requireNonNull(type),
          sanitizeMarking(eventMarking)));
      return this;
    }

    public DCRGraphBuilder addInputAction(String eventId, String action, String initiator, deprecated.dcr.ast.typing.Type type,
        Value inputValue) {
      this.addInputAction(eventId, action, initiator, type, new EventMarking(inputValue));
      return this;
    }

    public DCRGraphBuilder addInputAction(String eventId, String action, String initiator, deprecated.dcr.ast.typing.Type type) {
      this.addInputAction(eventId, action, initiator, type, new EventMarking());
      return this;
    }

    public DCRGraphBuilder addComputationAction(String eventId, String action, String initiator, ASTNode expression,
                                                deprecated.dcr.ast.typing.Type type, EventMarking marking) {
      events.add((graph) -> graph.addComputationAction(
          sanitizeAndRegisterEventId(eventId),
          sanitizeAction(action),
          sanitizeInitiator(initiator),
          expression,
          type,
          sanitizeMarking(marking)));
      return this;
    }

    public DCRGraphBuilder addComputationAction(String eventId, String action, String initiator, Value initialValue,
        ASTNode expression, deprecated.dcr.ast.typing.Type type) {
      return this.addComputationAction(eventId, action, initiator, expression, type, new EventMarking(initialValue));
    }

    public DCRGraphBuilder addComputationAction(String eventId, String action, String initiator,
        ASTNode expression, deprecated.dcr.ast.typing.Type type) {
      return this.addComputationAction(eventId, action, initiator, expression, type, new EventMarking());
    }

    public DCRGraphBuilder addComputationSendOperation(String eventId, String action, String initiator,
                                                       Set<String> receivers,
                                                       ASTNode expression, deprecated.dcr.ast.typing.Type type, EventMarking marking) {
      String sanitizedInitiator = sanitizeInitiator(initiator);
      events.add((graph) -> graph.addComputationSendOperation(
          sanitizeAndRegisterEventId(eventId),
          sanitizeAction(action),
          sanitizedInitiator,
          sanitizeReceivers(sanitizedInitiator, receivers),
          expression,
          type,
          sanitizeMarking(marking)));
      return this;
    }

    public DCRGraphBuilder addComputationSendOperation(String eventId, String action, String initiator,
                                                       Set<String> receivers,
                                                       ASTNode expression, deprecated.dcr.ast.typing.Type type, Value initialValue) {
      this.addComputationSendOperation(eventId, action, initiator, receivers, expression, type,
          new EventMarking(initialValue));
      return this;
    }

    public DCRGraphBuilder addComputationSendOperation(String eventId, String action, String initiator,
        Set<String> receivers, ASTNode expression, deprecated.dcr.ast.typing.Type type) {
      this.addComputationSendOperation(eventId, action, initiator, receivers, expression, type, new EventMarking());
      return this;
    }

    public DCRGraphBuilder addInputSendOperation(String eventId, String action, String initiator, Set<String> receivers,
                                                 deprecated.dcr.ast.typing.Type typeExpression, EventMarking marking) {
      String sanitizedInitiator = sanitizeInitiator(initiator);
      events.add((graph) -> graph.addInputSendOperation(
          sanitizeAndRegisterEventId(eventId),
          sanitizeAction(action),
          sanitizedInitiator,
          sanitizeReceivers(sanitizedInitiator, receivers),
          typeExpression,
          sanitizeMarking(marking)));
      return this;
    }

    public DCRGraphBuilder addInputSendOperation(String eventId, String action, String initiator, Set<String> receivers,
                                                 deprecated.dcr.ast.typing.Type typeExpression, Value initialValue) {
      this.addInputSendOperation(eventId, action, initiator, receivers, typeExpression, new EventMarking(initialValue));
      return this;
    }

    public DCRGraphBuilder addInputSendOperation(String eventId, String action, String initiator,
        Set<String> receivers, deprecated.dcr.ast.typing.Type typeExpression) {
      this.addInputSendOperation(eventId, action, initiator, receivers, typeExpression, new EventMarking());
      return this;
    }

    public DCRGraphBuilder addReceiveOperation(String eventId, String action, String initiator, String receiver,
                                               deprecated.dcr.ast.typing.Type type, EventMarking marking) {
      String sanitizedInitiator = sanitizeInitiator(initiator);
      events.add((graph) -> graph.addReceiveOperation(
          sanitizeAndRegisterEventId(eventId),
          sanitizeAction(action),
          sanitizedInitiator,
          sanitizeReceiver(sanitizedInitiator, receiver),
          type,
          sanitizeMarking(marking)));
      return this;
    }

    public DCRGraphBuilder addReceiveOperation(String eventId, String action, String initiator, String receiver,
                                               deprecated.dcr.ast.typing.Type type, Value value) {
      this.addReceiveOperation(eventId, action, initiator, receiver, type, new EventMarking(value));
      return this;
    }

    public DCRGraphBuilder addReceiveOperation(String eventId, String action, String initiator, String receiver,
        deprecated.dcr.ast.typing.Type type) {
      this.addReceiveOperation(eventId, action, initiator, receiver, type, new EventMarking());
      return this;
    }

    public DCRGraphBuilder addRelation(Type type, String sourceEventId, String targetEventId) {
      relations.add((graph) -> graph.addRelation(
          sanitizeRelationType(type),
          sanitizeAndRegisterRelationEventId(sourceEventId),
          sanitizeAndRegisterRelationEventId(targetEventId)));
      return this;
    }

    // TODO [awaiting spwan formalization]
    public DCRGraphBuilder beginSpawn(String eventId) {
      return null;
    }

  }
}