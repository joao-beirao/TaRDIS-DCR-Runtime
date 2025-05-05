package dcr1.runtime;

import dcr1.common.Environment;
import dcr1.common.data.computation.BooleanExpression;
import dcr1.common.data.values.BoolVal;
import dcr1.common.data.values.Value;
import dcr1.common.events.userset.values.UserVal;
import dcr1.model.GraphModel;
import dcr1.model.events.ComputationEventElement;
import dcr1.model.events.EventElement;
import dcr1.model.events.InputEventElement;
import dcr1.model.events.ReceiveEventElement;
import dcr1.model.relations.IControlFlowRelationElement;
import dcr1.model.relations.ISpawnRelationElement;
import dcr1.runtime.communication.CommunicationLayer;
import dcr1.runtime.elements.events.EventInstance;
import dcr1.runtime.elements.events.LocallyInitiatedEventInstance;
import dcr1.runtime.elements.events.RemotelyInitiatedEventInstance;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class GraphRunner {

    private enum InteractionType {
        TX, RX, NONE
    }

    private record EventId(String eventId, InteractionType interactionType) {
    }

    private static final String SELF = "@self";

    private final Map<EventId, EventInfo<? extends GenericEventInstance>> eventsByUuid;
    // ==
    // convenience mappings
    private final Map<String, EventInfo<ComputationInstance>> computationEvents;
    private final Map<String, EventInfo<InputInstance>> inputEvents;
    private final Map<String, EventInfo<ReceiveInstance>> receiveEvents;

    private final Map<EventInstance, List<ControlFlowRelationInfo>> controlFlowRelations;
    // ==
    // convenience mappings
    // -> (outgoing direction) relations for which the execution of the 'source'
    // event may affect the state of the 'target' event - indexed by 'source' event
    private final Map<EventInstance, List<ControlFlowRelationInfo>> includes;
    private final Map<EventInstance, List<ControlFlowRelationInfo>> excludes;
    private final Map<EventInstance, List<ControlFlowRelationInfo>> responses;
    // -> (incoming direction) - relations for which the 'source' event can actively
    // restrict the execution of the 'target' event - indexed by 'target' event
    private final Map<EventInstance, List<ControlFlowRelationInfo>> conditions;
    private final Map<EventInstance, List<ControlFlowRelationInfo>> milestones;
    // event ids mapped to spawn models
    // private final Map<EventInstance, List<InstantiatedSpawnRelation>> spawnRelations;
    private final Map<EventInstance, List<SpawnRelationInfo>> spawnRelations;
    // TODO [revisit] temporary setup to handle send-then-spawn

    private final UserVal owner;
    private final CommunicationLayer communicationLayer;
    // TODO [revisit] not really using the model after init
    private GraphModel graphModel;

    private static <K, V> void addToListMapping(K key, V value, Map<K, List<V>> mapping) {
        List<V> values = mapping.getOrDefault(key, new LinkedList<>());
        if (values.isEmpty()) {
            mapping.put(key, values);
        }
        values.add(value);
    }

    private static String newEventUuidOf(String localId, String idExtension) {
        return "_" + localId + (idExtension.isBlank() ? "" : "_" + idExtension);
    }


    private static <V> void bindIfAbsent(Environment<V> env, String identifier, V value) {
        if (env.bindIfAbsent(identifier, value).isPresent()) {
            throw new IllegalStateException(
                    "Internal error: environment binding should not already exist (" + identifier +
                            ")");
        }
    }

    private static <V> V lookupPresent(Environment<V> env, String identifier) {
        return env.lookup(identifier)
                .map(Environment.Binding::value)
                .orElseThrow(() -> new IllegalStateException(
                        "Internal error: environment binding should not be missing (" + identifier +
                                ")"));
    }

    private static <V> void rebindPresent(String identifier, V newValue, Environment<V> env) {
        env.rebindIfPresent(identifier, newValue)
                .orElseThrow(() -> new IllegalStateException(
                        "Internal error: environment binding should not be missing (" + identifier +
                                ")"))
                .setValue(newValue);
    }

    public GraphRunner(UserVal owner, CommunicationLayer communicationLayer) {
        this.owner = owner;
        this.communicationLayer = communicationLayer;
        this.eventsByUuid = new HashMap<>();
        this.computationEvents = new HashMap<>();
        this.inputEvents = new HashMap<>();
        this.receiveEvents = new HashMap<>();
        this.controlFlowRelations = new HashMap<>();
        includes = new HashMap<>();
        excludes = new HashMap<>();
        responses = new HashMap<>();
        conditions = new HashMap<>();
        milestones = new HashMap<>();
        spawnRelations = new HashMap<>();
    }


    // private static User evalParams(UserSetExprElement expr, En) {}


    // TODO rename remoteParticipants env arg & rename method
    // private static UserSetExprInstance newUserSetExprInstanceOf(UserSetExprElement expression,
    //         Environment<User> remoteParticipants, Environment<Value> evalEnv) {
    //     // TODO [revisit] exceptions being thrown here would signal an implementation issue
    //     return switch (expression) {
    //         case dcr1.model.events.userset.User user -> {
    //             var evalParams = Record.<Value<? extends PrimitiveType>>builder();
    //             user.params()
    //                     .fields()
    //                     .stream()
    //                     .map(field -> Record.Field.of(field.name(), field.value().eval(evalEnv)))
    //                     .forEach(evalParams::addField);
    //             for (var param : user.params().fields()) {
    //                 evalParams.addField(param.name(), param.value().eval(evalEnv));
    //             }
    //             yield new User(user.role(), RoleParams.of(evalParams.build()));
    //         }
    //         case dcr1.model.events.userset.Role role -> new Role(role.role());
    //         case Sender sender -> remoteParticipants.lookup(sender.eventId()).orElseThrow()
    //         .value();
    //         case Receiver receiver ->
    //                 remoteParticipants.lookup(receiver.eventId()).orElseThrow().value();
    //         // TODO [implement]
    //         case SetUnionExpr union -> new UnionSetExpr(union.userSetExprs()
    //                 .stream()
    //                 .map(expr -> newUserSetExprInstanceOf(expr, remoteParticipants, evalEnv))
    //                 .toList());
    //         case dcr1.model.events.userset.SetDiffExpr diff -> SetDiffExpr.of(
    //                 newUserSetExprInstanceOf(diff.positiveSet(), remoteParticipants, evalEnv),
    //                 newUserSetExprInstanceOf(diff.negativeSet(), remoteParticipants, evalEnv));
    //     };
    // }

// ------------------------------------------------------------------------
// ------------------------------------------------------- executing events
//


    public void init(GraphModel graphModel) {
        this.graphModel = graphModel;
        // TODO initialize spawn context
        instantiateGraphElement(Objects.requireNonNull(graphModel), SpawnContext.init(this.owner),
                "");
    }

    // comment: DCR semantics on eventInfo execution
    // IMPORTANT! computation expressions and guard expressions currently leverage primitive values
    // and references to eventInfo-data values. Effects apply in the following order
    //
    // 1. update the value of the eventInfo - for a computation eventInfo this means evaluating the
    // computation expression;
    // 2. set eventInfo as 'executed' and 'not-pending'
    // 3. Propagate side effects (relations) - all guards evaluated against the previous
    // marking, except for the computation eventInfo being executed, for which the updated
    // marking is
    // taken instead
    // ..3.1 Evaluate responses first: if there's a self-response relation, the eventInfo becomes
    // pending again
    // ..3.2 Evaluate includes/excludes next: add all included events first, and remove all
    // excluded events after - this means exclusion wins over inclusion, whatever the order in
    // which the relations are defined
    // ..3.3 go through spawns last
    // For the sake of simplicity, we accumulate results and apply in order in the end
    // TODO [add feature] relation guards
    private void onPropagateControlFlowConstraints(EventInfo eventInfo,
            ExecutionResult result) {
        // TODO later - consider accumulating effects as opposed to immediately applying them
        // List<Callable<GenericEventInstance>> stateUpdates = new LinkedList<>();
        // responses.getOrDefault(eventInfo, Collections.emptyList()).forEach(response -> {
        //     var target = response.target();
        //     if (target.isPending()) {
        //         result.addModifiedEvents(target);
        //         stateUpdates.add(() -> {
        //             target.onResponse();
        //             return target;
        //         });
        //     }
        // });
        responses.getOrDefault(eventInfo.event, Collections.emptyList()).forEach(response -> {
            if (!response.target().isPending()) {
                // TODO test guards
                if (response.guard()
                        .eval(eventInfo.evalContext.valueEnv())
                        .equals(BoolVal.of(true))) {
                    result.addModifiedEvents(response.target());
                    response.target().onResponse();
                }
            }
        });
        includes.getOrDefault(eventInfo.event, Collections.emptyList()).forEach(include -> {
            if (!include.target().isIncluded()) {
                // TODO guard
                result.addModifiedEvents(include.target());
                include.target().onInclude();
            }
        });
        excludes.getOrDefault(eventInfo.event, Collections.emptyList()).forEach(exclude -> {
            if (exclude.target().isIncluded()) {
                // TODO guard
                result.addModifiedEvents(exclude.target());
                exclude.target().onExclude();
            }
        });
    }

    // for local events only
    private static String localIdExtensionOf(String graphElementId, String idTokenExtension) {
        System.err.println("local");
        return String.format("%s_%s", graphElementId, idTokenExtension);
    }

    // for Tx/Rx events only
    // !! only OK if User determines a unique node!! Not sure if this is what we want, probably
    // will need to add a UUID to the mix (host name or similar)
    private static String globalIdExtensionOf(String graphElementId, String idExtensionToken,
            UserVal sender, UserVal receiver) {
        System.err.println("remote");
        return String.format("%s_%s", localIdExtensionOf(graphElementId, idExtensionToken),
                Integer.toHexString(sender.hashCode() + receiver.hashCode()));
    }


    // onLocal -> onActiveSpawn
    // onSend -> onActiveSpawn
    // onReceive

    // private void onExecute(EventInfo info, Value newValue, ExecutionResult result) {
    //     GenericEventInstance event = info.event;
    //     var hasExecuted = event.hasExecuted();
    //     event.onExecuted(newValue);
    //     if (!hasExecuted) {conditions.remove(event);}
    //     rebindPresent(event.getLocalId(), newValue, info.evalEnv);
    //     result.addModifiedEvents(event);
    //     onPropagateControlFlowConstraints(event, result);
    // }

    // private ExecutionResult onLocalExecute(EventInfo info, Value newValue) {
    //     ExecutionResult result = new ExecutionResult();
    //     onExecute(info, newValue, result);
    //     LocallyInitiatedEventInstance inst = (LocallyInitiatedEventInstance) info.event();
    //     return result;
    // }
    //
    // private ExecutionResult onSendExecute(EventInfo info, Value newValue) {
    //     ExecutionResult result = new ExecutionResult();
    //     onExecute(info, newValue, result);
    //     return result;
    // }
    //
    // private ExecutionResult onReceiveExecute(EventInfo<ReceiveInstance> info,
    //         Value newValue) {
    //     ExecutionResult result = new ExecutionResult();
    //     onExecute(info, newValue, result);
    //     return result;
    // }
    // updateEvent
    //
    //
    // private void executeEvent(GenericEventInstance event, Environment<Value> evalEnv,
    //         Value newValue) {
    //
    // }
    //
    // private void applyStateBasedControlFlowRelations(EventInstance event,
    //         ExecutionResult result) {
    //
    // }


    // TODO [revisit] updateEnv exception should reflect an implementation error: bug, not a feature
    // TODO [revisit] removal of conditions upon first execute
    private void locallyUpdateOnEventExecution(EventInfo eventInfo, ExecutionResult result,
            Value newValue) {

        GenericEventInstance event = eventInfo.event;
        var hasExecuted = event.hasExecuted();
        event.onExecuted(newValue);
        if (!hasExecuted) {conditions.remove(event);}
        rebindPresent(event.localId(), newValue, eventInfo.valueEnv());
        // rebindPresent(event.localId(), newValue.wrap(event.label()), eventInfo.valueEnv());
        result.addModifiedEvents(event);

        onPropagateControlFlowConstraints(eventInfo, result);
        result.setMarking(event.marking());
    }

    // TODO [proper exceptions]
    private void assertNonNullEnabledEvent(EventInfo info) {
        if (info == null) {throw new RuntimeException("Event not found");}
        if (!isEnabled(info.event)) {throw new RuntimeException("Event is not enabled");}
    }

    // TODO [proper exception]
    // TODO [revisit] moved to Marking?
    private void assertAdmissibleValueType(GenericEventInstance event, Value replaceValue) {
        // check different value type for input/receive event (subtyping now accounted for)
        if (!event.value().type().equals(replaceValue.type())) {
            System.err.printf("unexpected value %s, expecting %s%n", replaceValue,
                    event.value());
            System.err.printf("unexpected type %s, expecting %s%n", replaceValue.type(),
                    event.value().type());
            throw new RuntimeException("Illegal value type");
        }
    }

    // something to append to the event id, along with other identifiers - might change in the
    // future
    private static String generateIdExtensionToken() {
        return String.valueOf(System.currentTimeMillis()) + Math.round(Math.random() * 1000);
    }

    // TODO [revisit] using hashCode() may be fragile - could leverage a Babel returned UUID
    //  (something that is unique to this node), but may break separation of concerns...
    // private static String idExtensionOf(String eventId, String localUidToken, User sender,
    //         User receiver) {
    //     return String.format("_%s_%s_%s_%s", eventId, localUidToken, sender.hashCode(),
    //             receiver.hashCode());
    // }


    // called by Receive events
    private void onRemotelyInitiatedEvent(RemotelyInitiatedEventInstance event, UserVal sender,
            String idExtensionToken) {
        spawnRelations.getOrDefault(event, Collections.emptyList())
                .forEach(info -> onSpawn(event, info, sender, owner, idExtensionToken, false));
    }

    // ancillary call
    private void onLocallyInitiatedEvent(LocallyInitiatedEventInstance event, UserVal receiver,
            String idExtensionToken) {
        // TODO IFC constraint
        spawnRelations.getOrDefault(event, Collections.emptyList())
                .forEach(info -> onSpawn(event, info, owner, receiver, idExtensionToken, true));
    }

    // called by either Input or Computation events
    private void onLocallyInitiatedEvent(LocallyInitiatedEventInstance event,
            EvalContext evalCtxt) {
        // TODO IFC constraint
        var idExtensionToken = generateIdExtensionToken();
        event.receivers().ifPresentOrElse(rcvExpr -> {
            var rcvVal = rcvExpr.eval(evalCtxt.valueEnv(), evalCtxt.userEnv());
            // System.err.println("receiver: " + rcvVal);
            Set<UserVal> receivers = communicationLayer.uponSendRequest(event.getGlobalId(), rcvVal,
                    event.marking(), idExtensionToken);
            System.err.println("membership eval for receivers: " + receivers);
            receivers.forEach(
                    receiver -> onLocallyInitiatedEvent(event, receiver, idExtensionToken));
        }, () -> onSpawn((GenericEventInstance) event, idExtensionToken));
    }

    // local
    private void onSpawn(GenericEventInstance event, String idExtensionToken) {
        var spawns = spawnRelations.getOrDefault(event, new LinkedList<>());
        if (spawns.isEmpty()) {return;}
        spawns.forEach(info -> {
            var subgraph = info.spawn().getSubgraph();
            var newSpawnContext = info.spawnContext().beginScope(event);
            // var subgraphCounter = fetchUpdateCounter(subgraph.getElementId());
            // var uidExtension = extensionIdOf(subgraph.getElementId(), subgraphCounter, owner);
            var idExtension = localIdExtensionOf(subgraph.getElementId(), idExtensionToken);
            instantiateGraphElement(subgraph, newSpawnContext, idExtension);
        });
    }

    private void onSpawn(EventInstance event, SpawnRelationInfo info, UserVal sender,
            UserVal receiver,
            String idExtensionToken, boolean locallyInitiated) {
        var remoteUser = locallyInitiated ? receiver : sender;
        var subgraph = info.spawn().getSubgraph();
        var newSpawnContext = info.spawnContext().beginScope(event, remoteUser);
        var localIdExtension =
                globalIdExtensionOf(subgraph.getElementId(), idExtensionToken, sender, receiver);
        instantiateGraphElement(subgraph, newSpawnContext, localIdExtension);
    }


    // ====== Entry points ======

    // TODO [revisit] is an UndeclaredIdentifierException acceptable here?
    // TODO [revisit] validate args? (public method)
    public ExecutionResult executeComputationEvent(String eventId) {
        // TODO (follow lock protocol?)
        var info = computationEvents.get(eventId);
        assertNonNullEnabledEvent(info);
        var event = info.event();
        Value recomputedValue = event.getComputationExpression().eval(info.evalContext()
                .valueEnv());
        ExecutionResult executionResult = new ExecutionResult();
        // TODO [tmp] IFC evaluation duplicated and unfriendly - extract method and return proper
        //  result
        if (!event.ifcConstraint().eval(info.evalContext.valueEnv()).value()) {
            throw new RuntimeException("IFC leak");
        }
        locallyUpdateOnEventExecution(info, executionResult, recomputedValue);
        onLocallyInitiatedEvent(event, info.evalContext);
        return executionResult;
    }


    // TODO [revisit] validate args (public method)
    // TODO throw custom exceptions - NoSuchEvent, BadInputType, EventNotEnabled
    // TODO better naming for [?] input events
    // executing a non-empty input event (on that accepts input)
    public ExecutionResult executeInputEvent(String eventId, Value inputValue) {
        // TODO (follow lock protocol?)
        var info = inputEvents.get(eventId);
        assertNonNullEnabledEvent(info);
        assertAdmissibleValueType(info.event, inputValue);
        var event = info.event();
        ExecutionResult executionResult = new ExecutionResult();
        // TODO [tmp] IFC evaluation duplicated and unfriendly - extract method and return proper
        //  result
        if (!event.ifcConstraint().eval(info.evalContext.valueEnv()).value()) {
            throw new RuntimeException("IFC leak");
        }
        locallyUpdateOnEventExecution(info, executionResult, inputValue);
        onLocallyInitiatedEvent(event, info.evalContext);
        return executionResult;
    }

    // TODO [revisit] validate args (public method)
    // TODO throw custom exceptions - NoSuchEvent, BadInputType, EventNotEnabled
    public ExecutionResult executeInputEvent(String eventId) {
        // TODO (follow lock protocol??)
        var info = inputEvents.get(eventId);
        assertNonNullEnabledEvent(info);
        // TODO [revisit]
        var event = info.event();
        // var voidInput = Undefined.ofVoid();
        // assertAdmissibleValueType(info.event(), voidInput);
        Value voidInput = null;
        assertAdmissibleValueType(info.event(), voidInput);
        ExecutionResult executionResult = new ExecutionResult();
        // TODO [tmp] IFC evaluation duplicated and unfriendly - extract method and return proper
        //  result
        if (!event.ifcConstraint().eval(info.evalContext.valueEnv()).value()) {
            throw new RuntimeException("IFC leak");
        }
        locallyUpdateOnEventExecution(info, executionResult, voidInput);
        //
        onLocallyInitiatedEvent(info.event(), info.evalContext);
        return executionResult;
    }


    // TODO [revisit] is an UndeclaredIdentifierException acceptable here?
    // TODO [revisit] validate args? (public method)
    // TODO [revisit] enabledness?
    // TODO [properly handle event not found]
    // TODO [revisit] accept subtypes
    public ExecutionResult onReceiveEvent(String eventId, Value receivedValue, UserVal sender,
            String idExtensionToken) {
        var info = receiveEvents.get(eventId);
        assertNonNullEnabledEvent(info);
        assertAdmissibleValueType(info.event, receivedValue);
        var event = info.event();
        ExecutionResult executionResult = new ExecutionResult();
        locallyUpdateOnEventExecution(info, executionResult, receivedValue);
        onRemotelyInitiatedEvent(event, sender, idExtensionToken);
        return executionResult;
    }

    private boolean isEnabled(EventInstance event) {
        // TODO [revise] should exclude receive events - they are triggered from by an external
        //  participant
        if (!event.isIncluded()) {return false;}

        for (var rel : conditions.getOrDefault(event, Collections.emptyList())) {
            if (rel.source().isIncluded() && !rel.source().hasExecuted()) {return false;}
        }

        for (var rel : milestones.getOrDefault(event, Collections.emptyList())) {
            if (rel.source().isIncluded() && rel.source().isPending()) {return false;}
        }
        return true;
    }

    // TODO [revisit] exception catching - spawns should also go through this
    // TODO [revise] the motivation for collecting state updates
    // TODO [revise]
    // instantiate a (sub)graph model element
    private void instantiateGraphElement(GraphModel graph, SpawnContext spawnContext,
            String uidExtension) {
        List<Consumer<GraphRunner>> updates = new LinkedList<>();
        graph.events()
                .forEach(element -> updates.add(
                        runner -> runner.instantiateEventElement(element, uidExtension,
                                spawnContext)));
        graph.controlFlowRelations()
                .forEach(element -> updates.add(
                        runner -> runner.instantiateControlFlowRelationElement(element,
                                spawnContext)));
        graph.spawnRelations()
                .forEach(element -> updates.add(
                        runner -> runner.instantiateSpawnRelation(element, spawnContext)));
        updateState(updates);
    }

    private InstantiatedSpawnRelation newSpawnRelationInstanceOf(ISpawnRelationElement baseElement,
            SpawnContext spawnContext) {
        // TODO [revise] handle lookup exception uniformly (this could only result of an
        //  implementation error - not something to throw and handle externally
        GenericEventInstance source =
                lookupPresent(spawnContext.alphaRenamings, baseElement.getSourceId());
        return Relations.newSpawnRelationInstance(baseElement, source);
    }

    private void instantiateSpawnRelation(ISpawnRelationElement baseElement,
            SpawnContext spawnContext) {
        if (!canInstantiate(baseElement.instantiationConstraint(), spawnContext.evalEnv)) {
            System.err.println("Unable to instantiate " + baseElement.getElementId());
        }
        else {
            InstantiatedSpawnRelation instance =
                    newSpawnRelationInstanceOf(baseElement, spawnContext);
            List<SpawnRelationInfo> spawnRelations =
                    this.spawnRelations.getOrDefault(instance.getSource(), new LinkedList<>());
            spawnRelations.add(new SpawnRelationInfo(instance, spawnContext));
            this.spawnRelations.putIfAbsent(instance.getSource(), spawnRelations);
        }
    }

    private InstantiatedControlFlowRelation newControlFlowRelationInstanceOf(
            IControlFlowRelationElement baseElement, SpawnContext spawnContext) {
        // TODO [revise] exception throwing - throwing here signals an implementation error -
        //  should probably funnel this into an InternalErrorException for similar methods
        GenericEventInstance source =
                spawnContext.alphaRenamings.lookup(baseElement.getSourceId()).orElseThrow().value();
        GenericEventInstance target =
                spawnContext.alphaRenamings.lookup(baseElement.getTargetId()).orElseThrow().value();
        return Relations.newControlFlowRelation(baseElement, source, target);
    }

    private void instantiateControlFlowRelationElement(IControlFlowRelationElement baseElement,
            SpawnContext spawnContext) {
        if (!canInstantiate(baseElement.instantiationConstraint(), spawnContext.evalEnv)) {
            System.err.println("Unable to instantiate " + baseElement.getElementId());
        }
        else {
            InstantiatedControlFlowRelation instance =
                    newControlFlowRelationInstanceOf(baseElement, spawnContext);
            ControlFlowRelationInfo value =
                    new ControlFlowRelationInfo(instance, spawnContext.evalEnv);
            addToListMapping(instance.getSource(), value, controlFlowRelations);
            switch (instance.getRelationType()) {
                case INCLUDE -> addToListMapping(instance.getSource(), value, includes);
                case EXCLUDE -> addToListMapping(instance.getSource(), value, excludes);
                case RESPONSE -> addToListMapping(instance.getSource(), value, responses);
                case CONDITION -> addToListMapping(instance.getTarget(), value, conditions);
                case MILESTONE -> addToListMapping(instance.getTarget(), value, milestones);
            }
        }
    }

    private boolean canInstantiate(BooleanExpression constraint, Environment<Value> evalEnv) {
        return constraint.eval(evalEnv).equals(BoolVal.of(true));
    }

    // TODO [revisit] also add to specific computation/input/receive mappings?
    // updates the graph's state by instantiating an event element
    private void instantiateEventElement(EventElement baseElement, String idExtension,
            SpawnContext spawnContext) {
        if (!canInstantiate(baseElement.instantiationConstraint(), spawnContext.evalEnv)) {
            System.err.println("Unable to instantiate " + baseElement.getElementId());
            return;
        }
        else {System.err.println("Instantiating " + baseElement.getElementId());}
        String uuid = newEventUuidOf(baseElement.localId(), idExtension);
        GenericEventInstance instance;
        EventInfo eventInfo;
        switch (baseElement) {
            case ComputationEventElement elem -> {
                instance = Events.newComputationInstance(uuid, elem);
                EventInfo<ComputationInstance> info =
                        new EventInfo<>((ComputationInstance) instance,
                                new EvalContext(spawnContext.evalEnv,
                                        spawnContext.triggerCounterparts));
                // TODO refactor
                if (((ComputationInstance) instance).receivers().isEmpty()) {
                    eventsByUuid.put(new EventId(uuid, InteractionType.NONE), info);
                }
                else {
                    eventsByUuid.put(new EventId(uuid, InteractionType.TX), info);
                }
                computationEvents.put(instance.getGlobalId(), info);
                eventInfo = info;
            }
            case InputEventElement elem -> {
                instance = Events.newInputInstance(uuid, elem);
                EventInfo<InputInstance> info =
                        new EventInfo<>((InputInstance) instance,
                                new EvalContext(spawnContext.evalEnv,
                                        spawnContext.triggerCounterparts));
                if (((InputInstance) instance).receivers().isEmpty()) {
                    eventsByUuid.put(new EventId(uuid, InteractionType.NONE), info);
                }
                else {
                    eventsByUuid.put(new EventId(uuid, InteractionType.TX), info);
                }
                inputEvents.put(instance.getGlobalId(), info);
                eventInfo = info;
            }
            case ReceiveEventElement elem -> {
                instance = Events.newReceiveInstance(uuid, elem);
                EventInfo<ReceiveInstance> info =
                        new EventInfo<>((ReceiveInstance) instance,
                                new EvalContext(spawnContext.evalEnv,
                                        spawnContext.triggerCounterparts));
                eventsByUuid.put(new EventId(uuid, InteractionType.RX), info);
                receiveEvents.put(instance.getGlobalId(), info);
                eventInfo = info;
            }
        }
        Value instanceValue = instance.marking().value();

        // TODO use bindIfAbsent above
        eventInfo.valueEnv()
                .bindIfAbsent(instance.localId(), instanceValue);
        // .wrap(baseElement.label()));
        spawnContext.alphaRenamings.bindIfAbsent(baseElement.localId(), instance);
    }


    // Applies a sequence of updates to the graph's state as a single update step; each update
    // step should leave the graph in a consistent state (or else...)
    private void updateState(Iterable<Consumer<GraphRunner>> updates) {
        updates.forEach(update -> update.accept(this));
    }


    // take a set expression, as expressed by the model, (along with some environment maybe?) to
    //
    // note: an expression such as Receiver(e2) only makes sense for an event in the model
    // private List<User> evalUserSetExpressions(UserSetExprElement participants) {
    //     throw new RuntimeException("Not yet implemented");
    // }
    // evaluation of communication participants (aka, receivers) for the purpose of performing
    // sends: this should only consist of roles, users and unions or diffs of the former
    // + Initiator and Receiver exprs are resolved on instantiation according to the environment;
    // + Necessarily EXTERNAL to the graph: resolving role expressions into actual users
    // + Plain computation that can be resolved anywhere - resolving Set operations: union & except

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(eventsByUuid.values()
                .stream()
                .map(ctxt -> ctxt.event.toString())
                .collect(Collectors.joining("\n", "\n", "")));
        if (!controlFlowRelations.isEmpty()) {
            builder.append(System.lineSeparator()).append(";");
            controlFlowRelations.values()
                    .forEach(listing -> listing.forEach(
                            relInfo -> builder.append(System.lineSeparator())
                                    .append(relInfo.relation().toString())));
        }
        if (!spawnRelations.isEmpty()) {
            builder.append(System.lineSeparator()).append(";");
            spawnRelations.values()
                    .forEach(list -> list.stream()
                            .map(info -> "\n" + info.spawn.baseElement().getSourceId() +
                                    " -->> " + info.spawn.getSubgraph())
                            .forEach(builder::append));
        }
        return builder.toString();
    }

    public String unparse(String indent) {
        Objects.requireNonNull(indent);
        StringBuilder builder = new StringBuilder();
        builder.append("   == Runtime Graph State ==\n");
        Consumer<Map<EventInstance, List<ControlFlowRelationInfo>>> ctrlFlowRelUnparser =
                infoVals -> {
                    infoVals.values()
                            .stream()
                            .map(values -> values.stream()
                                    .map(info -> info.relation.unparse(indent))
                                    .collect(Collectors.joining("\n")))
                            .forEach(builder::append);
                };
        Consumer<Map<EventInstance, List<SpawnRelationInfo>>> spawnRelUnparser = infoVals -> {
            infoVals.values()
                    .forEach(list -> list.stream()
                            .map(info -> info.spawn.unparse(indent) + "\n")
                            .forEach(builder::append));
        };
        eventsByUuid.values()
                .stream()
                .map(ctxt -> ctxt.event.unparse(indent) + "\n")
                .forEach(builder::append);
        ctrlFlowRelUnparser.accept(conditions);
        ctrlFlowRelUnparser.accept(responses);
        spawnRelUnparser.accept(spawnRelations);
        return builder.toString();
    }

    /**
     * @param evalEnv
     *         cumulative register keeping track of alpha-renaming, sender/receiver of triggering
     *         event (when applicable), and eval env
     */
    record SpawnContext(Environment<Value> evalEnv,
                        Environment<GenericEventInstance> alphaRenamings,
                        Environment<UserVal> triggerCounterparts) {
        // cumulative register keeping track of actual sender/receiver of each interaction
        // triggering a spawn (either Tx or Rx) - enables resolving of Receiver(e1)
        // and Sender(e1) type of expressions - empty for top-level events

        // static SpawnContext empty() {
        //     return new SpawnContext(Environment.empty(), Environment.empty(), Environment
        //     .empty());
        // }

        static SpawnContext init(UserVal self) {
            var selfVal = self.getParamsAsRecordVal();
            Environment<Value> evalEnv = Environment.empty();
            // @self must always be available for evaluation of constraints
            evalEnv.bindIfAbsent(SELF, selfVal);
            return new SpawnContext(evalEnv, Environment.empty(), Environment.empty());
        }

        // TODO [revise]
        // TODO [revisit] alphaRenamings.beginScope() - why?
        // when the triggering event involves communication
        SpawnContext beginScope(EventInstance triggerEvent, UserVal triggerCounterpart) {
            var newEvalEnv = evalEnv.beginScope("@trigger",
                    triggerEvent.value());
            // .wrap(triggerEvent.label()));
            var newAlphaRenamings = alphaRenamings.beginScope();
            var newTriggerCounterparts =
                    triggerCounterparts.beginScope(triggerEvent.localId(), triggerCounterpart);
            // TODO defensive copy triggerVal - immutable snapshot
            return new SpawnContext(newEvalEnv, newAlphaRenamings, newTriggerCounterparts);
        }

        // when the triggering event is local
        // SpawnContext beginScope(Value triggerVal) {
        SpawnContext beginScope(EventInstance triggerEvent) {
            // TODO [rethink]] do we need to triggerCounterparts.beginScope()?
            // TODO defensive copy triggerVal - immutable snapshot
            var newEvalEnv = evalEnv.beginScope("@trigger",
                    triggerEvent.value());
            // .wrap(triggerEvent.label()));
            return new SpawnContext(newEvalEnv, alphaRenamings.beginScope(), triggerCounterparts);
        }
    }

    private record SpawnRelationInfo(InstantiatedSpawnRelation spawn, SpawnContext spawnContext) {}

    private record ControlFlowRelationInfo(InstantiatedControlFlowRelation relation,
                                           Environment<Value> evalEnv) {
        GenericEventInstance source() {
            return relation.getSource();
        }

        BooleanExpression guard() {return relation.getGuard();}

        GenericEventInstance target() {
            return relation.getTarget();
        }
    }

    private record EvalContext(Environment<Value> valueEnv, Environment<UserVal> userEnv) {}

    private record EventInfo<E extends GenericEventInstance>(E event,
                                                             EvalContext evalContext) {
        Environment<Value> valueEnv() {return evalContext().valueEnv;}

        Environment<UserVal> userEnv() {return evalContext().userEnv;}
        // TODO [revisit]
        // private <E1 extends GenericEventInstance> E1 tryGetEvent(Class<E1> clazz) {
        //     if (event.getClass().equals(clazz)) {return clazz.cast(event);}
        //     else {throw new RuntimeException();}
        // }
    }

    // private record EventInfo<E extends GenericEventInstance>(E event,
    //                                                             Environment<Value> evalEnv) {
    //     // TODO [revisit]
    //     private <E1 extends GenericEventInstance> E1 tryGetEvent(Class<E1> clazz) {
    //         if (event.getClass().equals(clazz)) {return clazz.cast(event);}
    //         else {throw new RuntimeException();}
    //     }
    // }
}
