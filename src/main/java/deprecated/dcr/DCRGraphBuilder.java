package deprecated.dcr;


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
 *
 *<br>
 *
 * <p>
 *
 * This builder acts as a buffer, enabling events and relations to be added
 * in any order in between {@link DCRGraphBuilder#build() build()} calls.
 * However, when {@link DCRGraphBuilder#build() build()} is called, every event
 * referenced in a relation must have also been added. Failing to do so wil
 * raise an error.
 *
 */
// TODO define errors
public final class DCRGraphBuilder {

    // private final List<Consumer<DCRGraphClass>> events;
    // private final List<Consumer<DCRGraphClass>> relations;
    // private final Set<String> usedIds;
    // private final String role;

    // public DCRGraphBuilder(String role) {
    //     this.events = new LinkedList<>();
    //     this.relations = new LinkedList<>();
    //     this.usedIds = new HashSet<>();
    //     this.role = role;
    // }

    // // public void init(String role) {
    // // graph = new DCRGraphClass(role);
    // // }

    // public DCRGraphClass build() {
    //     DCRGraphClass graph = new DCRGraphClass(role);
    //     // add all events first, and all relations after
    //     events.forEach((c) -> c.accept(graph));
    //     relations.forEach((c) -> c.accept(graph));
    //     return graph;
    // }

    // private static final String errorMsgNullArg(String argName) {
    //     return String.format("'%s' : cannot be null.", argName);
    // }

    // private static final String errorMsgBlankStringArg(String argName) {
    //     return String.format("'%s' : cannot be a blank String", argName);
    // }

    // private static final String errorMsgBlankStringArg(String argName, String argVal) {
    //     return String.format("'%s' = '%s' : event identifiers must be unique", argName, argVal);
    // }

    // private static final String errorMsgInitiatorAsReceiver(String argVal) {
    //     return String.format("%s : cannot declare participant as both an initiator and a receiver of a interaction.",
    //             argVal);
    // }

    // private <T> T requiresArgNotNull(T arg, String argName) {
    //     return Objects.requireNonNull(arg, errorMsgNullArg(argName));
    // }

    // private String sanitizeStringArg(String arg, String argName) {
    //     if (requiresArgNotNull(arg, argName).isBlank()) {
    //         throw new IllegalArgumentException(errorMsgBlankStringArg(argName));
    //     }
    //     return arg.trim();
    // }

    // private String sanitizeId(String eventId) {
    //     String argName = "eventId";
    //     String arg = sanitizeStringArg(eventId, argName);
    //     if (!usedIds.add(arg))
    //         throw new IllegalArgumentException(errorMsgBlankStringArg(argName, eventId));
    //     return arg;
    // }

    // private String sanitizeAction(String action) {
    //     return sanitizeStringArg(action, "action");
    // }

    // private String sanitizeInitiator(String role) {
    //     return sanitizeStringArg(role, "initiator role");
    // }

    // private EventMarking sanitizeMarking(EventMarking eventMarking) {
    //     return requiresArgNotNull(eventMarking, "eventMarking");
    // }

    // // assumes initiator was already sanitized
    // private String sanitizeReceiver(String sanitizeInitiator, String receiver) {
    //     String rcvr = sanitizeStringArg(receiver, "receiver role");
    //     if (sanitizeInitiator.equalsIgnoreCase(rcvr))
    //         throw new IllegalArgumentException(errorMsgInitiatorAsReceiver(rcvr));
    //     return rcvr;
    // }

    // private Set<String> sanitizeReceivers(String initiator, Set<String> receivers) {
    //     return receivers
    //             .stream()
    //             .map(((e) -> sanitizeReceiver(initiator, e)))
    //             .collect(Collectors.toCollection(HashSet::new));
    // }

    // private RelationType sanitizeRelationType(RelationType relationType) {
    //     return requiresArgNotNull(relationType, "relation type");
    // }

    // public DCRGraphBuilder addInputAction(String eventId, String action, String initiator, EventMarking eventMarking) {
    //     events.add((graph) -> graph.addInputAction(sanitizeId(eventId), sanitizeAction(action),
    //             sanitizeInitiator(initiator), sanitizeMarking(eventMarking)));
    //     return this;
    // }

    // public DCRGraphBuilder addInputAction(String eventId, String action, String initiator, Value initialValue) {
    //     this.addInputAction(eventId, action, initiator, new EventMarking(initialValue));
    //     return this;
    // }

    // public DCRGraphBuilder addInputAction(String eventId, String action, String initiator) {
    //     this.addInputAction(eventId, action, initiator, new EventMarking());
    //     return this;
    // }

    // public DCRGraphBuilder addComputationAction(String eventId, String action, String initiator, EventMarking marking) {
    //     events.add((graph) -> graph.addComputationAction(sanitizeId(eventId), sanitizeAction(action),
    //             sanitizeInitiator(initiator), sanitizeMarking(marking)));
    //     return this;
    // }

    // public DCRGraphBuilder addComputationAction(String eventId, String action, String initiator, Value value) {
    //     return this.addComputationAction(eventId, action, initiator, new EventMarking(value));
    // }

    // public DCRGraphBuilder addComputationAction(String eventId, String action, String initiator) {
    //     return this.addComputationAction(eventId, action, initiator, new EventMarking());
    // }

    // public DCRGraphBuilder addSendOperation(String eventId, String action, String initiator, Set<String> receivers,
    //         EventMarking marking) {
    //     String sanitizedInitiator = sanitizeInitiator(initiator);
    //     events.add((graph) -> graph.addSendOperation(sanitizeId(eventId), sanitizeAction(action),
    //             sanitizedInitiator, sanitizeReceivers(sanitizedInitiator, receivers), sanitizeMarking(marking)));
    //     return this;
    // }

    // public DCRGraphBuilder addSendOperation(String eventId, String action, String initiator, Set<String> receivers,
    //         Value value) {
    //     this.addSendOperation(eventId, action, initiator, receivers, new EventMarking(value));
    //     return this;
    // }

    // public DCRGraphBuilder addSendOperation(String eventId, String action, String initiator, Set<String> receivers) {
    //     this.addSendOperation(eventId, action, initiator, receivers, new EventMarking());
    //     return this;
    // }

    // public DCRGraphBuilder addReceiveOperation(String eventId, String action, String initiator, String receiver,
    //         EventMarking marking) {
    //     String sanitizedInitiator = sanitizeInitiator(initiator);
    //     events.add((graph) -> graph.addReceiveOperation(sanitizeId(eventId), sanitizeAction(action), sanitizedInitiator,
    //             sanitizeReceiver(sanitizedInitiator, receiver), sanitizeMarking(marking)));
    //     return this;
    // }

    // public DCRGraphBuilder addReceiveOperation(String eventId, String action, String initiator, String receiver,
    //         Value value) {
    //     this.addReceiveOperation(eventId, action, initiator, receiver, new EventMarking(value));
    //     return this;
    // }

    // public DCRGraphBuilder addReceiveOperation(String eventId, String action, String initiator, String receiver) {
    //     this.addReceiveOperation(eventId, action, initiator, receiver, new EventMarking());
    //     return this;
    // }

    // public DCRGraphBuilder addRelation(RelationType relationType, String sourceEvent, String targetEvent) {
    //     relations.add((graph) -> graph.addRelation(sanitizeRelationType(relationType), sanitizeId(sourceEvent),
    //             sanitizeId(targetEvent)));
    //     return this;
    // }
}
