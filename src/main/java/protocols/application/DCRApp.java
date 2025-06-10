package protocols.application;

import app.presentation.endpoint.EndpointDTO;
import app.presentation.mappers.EndpointMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import dcr.common.Record;
import dcr.common.data.types.BooleanType;
import dcr.common.data.types.IntegerType;
import dcr.common.data.types.StringType;
import dcr.common.data.types.Type;
import dcr.common.data.values.BoolVal;
import dcr.common.data.values.IntVal;
import dcr.common.data.values.StringVal;
import dcr.common.data.values.Value;
import dcr.common.events.Event;
import dcr.common.events.userset.values.UserSetVal;
import dcr.common.events.userset.values.UserVal;
import dcr.model.GraphElement;
import dcr.runtime.GraphRunner;
import dcr.runtime.communication.CommunicationLayer;
import dcr.runtime.communication.MembershipLayer;
import dcr.runtime.monitoring.GraphObserver;
import dcr.runtime.monitoring.StateUpdate;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import protocols.application.requests.AppRequest;
import protocols.application.requests.DCRAppRequest;
import protocols.dcr.DistributedDCRProtocol;
import pt.unl.di.novasys.babel.webservices.WebAPICallback;
import pt.unl.di.novasys.babel.webservices.application.GenericWebServiceProtocol;
import pt.unl.di.novasys.babel.webservices.utils.EndpointPath;
import pt.unl.di.novasys.babel.webservices.utils.GenericWebAPIResponse;
import pt.unl.fct.di.novasys.babel.exceptions.HandlerRegistrationException;
import pt.unl.fct.di.novasys.babel.generic.ProtoNotification;
import pt.unl.fct.di.novasys.babel.generic.ProtoReply;
import pt.unl.fct.di.novasys.babel.protocols.dissemination.notifications.BroadcastDelivery;
import pt.unl.fct.di.novasys.babel.protocols.dissemination.requests.BroadcastRequest;
import pt.unl.fct.di.novasys.babel.protocols.membership.notifications.NeighborUp;
import pt.unl.fct.di.novasys.babel.protocols.storage.notifications.JSONDataNotification;
import pt.unl.fct.di.novasys.babel.protocols.storage.replies.ExecuteStatusReply;
import pt.unl.fct.di.novasys.babel.protocols.storage.utils.operations.utils.CommonOperationStatus;
import pt.unl.fct.di.novasys.babel.protocols.storage.utils.operations.utils.CommonOperationType;
import pt.unl.fct.di.novasys.network.data.Host;
import rest.DCRGraphREST;
import rest.request.InputRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import pt.unl.fct.di.novasys.babel.protocols.storage.replies.ExecuteJSONReply;
// docker run example
// babel % docker run --network tardis-babel-backend-net --rm -h P_1_2 --name P_1_2 -it dcr-babel
// interface=eth0 role=P cid=1 pid=2

// TODO revisit bootstrap config process - currently loading a json resource based on role name
public final class DCRApp
        extends GenericWebServiceProtocol
        implements GraphObserver, CommunicationLayer {


    private static final Logger logger = LogManager.getLogger(DCRApp.class);

    private static final class LazyHolder {
        static final DCRApp INSTANCE = new DCRApp();
    }

    public static final String PROTOCOL_NAME = "DCRApp";
    public static final short PROTO_ID = 51;

    private static final int DEFAULT_PORT = 9000; // default port to listen on

    // TODO add support for endpoint.role config
    private static final String CLI_ROLE_ARG = "role";

    private GraphRunner runner = null;
    private Endpoint endpoint = null;
//    private Map<String, DCRAppRequest> pendingRequests =null;

    public static DCRApp getInstance() {
        return LazyHolder.INSTANCE;
        // return new DCRApp();
    }

    private static UserVal instantiateSelf(Properties props, Endpoint.Role roleDecl) {
        return UserVal.of(roleDecl.roleName(), Record.ofEntries(roleDecl.params()
                .stream()
                .map(param -> fetchSelfParamField(props, param.name(), param.value()))
                .collect(Collectors.toMap(Record.Field::name, Record.Field::value))));
    }

    private static Record.Field<Value> fetchSelfParamField(Properties props, String key,
                                                           Type type) {
        var prop = props.getProperty(key);
        return Record.Field.of(key, switch (type) {
            case BooleanType ignored -> BoolVal.of(Boolean.parseBoolean(prop));
            case IntegerType ignored -> IntVal.of(Integer.parseInt(prop));
            case StringType ignored -> StringVal.of(prop);
            default -> throw new IllegalStateException("Unexpected value for role param: " + type);
        });
    }

    private static Endpoint decodeEndpoint(String jsonEncodedEndpoint)
            throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        var deserializedEndpoint = objectMapper.readValue(jsonEncodedEndpoint, EndpointDTO.class);
        return EndpointMapper.mapEndpoint(deserializedEndpoint);
    }


    private DCRApp() {
        super(PROTOCOL_NAME, PROTO_ID);
//        this.pendingRequests = new HashMap<>();
    }

    @Override
    public void init(Properties properties) throws HandlerRegistrationException, IOException {
        logger.info("Initializing DCRApp");
        // request handlers
        registerRequestHandler(AppRequest.REQUEST_ID, this::uponReceiveDcrRequest);
        // reply handlers
        // registerReplyHandler(AppReply.REPLY_ID, this::onPongReply);
        // start CLI
        registerRequestHandler(BroadcastRequest.REQUEST_ID, this::uponBroadCastRequest);
        subscribeNotification(BroadcastDelivery.NOTIFICATION_ID, this::uponBroadcastDelivery);
        // triggerNotification(new NeighborUp(self));
        subscribeNotification(NeighborUp.NOTIFICATION_ID, this::uponNeighborUpNotification);

//        registerReplyHandler(ExecuteJSONReply.REPLY_ID, this::uponJsonReply);
//        registerReplyHandler(ExecuteStatusReply.REPLY_ID, this::uponStatusReply);

//        subscribeNotification(JSONDataNotification.NOTIFICATION_ID, this::uponDataNotification);

        // observation: Bootstrap is currently supported by CLI params:
        // - a 'role' param is required, and determines the role this endpoint should enact - based
        // on the role, the json-encoded endpoint resource is loaded and used to instantiate,
        // both the DCR Model and the Role for the active participant;
        // - similarly, CLI params are used to inject the runtime parameter values for the
        // role (when applicable), and are expected to follow the parameter names declared by the
        // selected endpoint.
        logger.info("role property: {}", properties.getProperty(CLI_ROLE_ARG));

        try (InputStream in = DCRApp.class.getResourceAsStream(
                String.format("%s.json", properties.getProperty(CLI_ROLE_ARG)))) {
            assert in != null;
            // the user associated with this endpoint
            UserVal self;
            // the behaviour this endpoint will enact
            GraphElement graphElement;
            {
                // load the information required to deploy this endpoint
                var jsonEncodedEndpoint = new String(in.readAllBytes(), StandardCharsets.UTF_8);
                var endpoint = decodeEndpoint(jsonEncodedEndpoint);
                this.endpoint = endpoint;
                // inject runtime parameters into self
                self = instantiateSelf(properties, endpoint.role());
                graphElement = endpoint.graphElement();
            }
            // aggregates CLI-based functionality and callbacks (replaceable with GUI/REST/...)
            CLI cmdLineRunner = new CLI(this);
            // setup graph runner
            runner = new GraphRunner(self, this);
            runner.registerGraphObserver(this);
            runner.init(graphElement);
            // start CLI-based interaction
            cmdLineRunner.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void uponBroadcastDelivery(ProtoNotification protoNotification, short i) {
        logger.info("Broadcast delivery for " + protoNotification.toString());
        logger.info("Protocol id {}", String.valueOf(i));
    }

    private void uponBroadCastRequest(BroadcastRequest broadcastRequest, short sourceProtocol) {
        logger.info("BroadCast Request: {}", broadcastRequest);
    }

    private void uponNeighborUpNotification(NeighborUp up, short protoID) {
        logger.info("NeighborUp notification triggered for protoID: {}", protoID);
        // if(protoID == EpidemicGlobalView.PROTOCOL_ID) return; //It is a good idea to ignore
        // notifications issued by ourselves
        //
        // if(this.status == Status.STOP) {
        //     setupTimer(new InitializeTimer(), this.initializeDelay);
        // }
    }
//
//    }
//    private void uponJsonReply(ExecuteJSONReply reply, short protoID) {
//        logger.warn(
//                "Received operation type {} with identifier {} from collection {} with status code {} and value {} and message {}",
//                reply.getOperationType(), reply.getObjectID(), reply.getCollection(), reply.getStatus(),
//                reply.getResult(), reply.getMessage());
//        String opID = reply.getObjectID();
//        DCRAppRequest info = this.pendingRequests.get(opID);
//        if (info == null)
//            return;
//
//        if (reply.getStatus() != CommonOperationStatus.OK) {
//            logger.error("Status: {}", reply.getStatus());
//            logger.error("Message: {}", reply.getMessage());
//
//            info.getCallback().triggerResponse(opID,
//                    new GenericWebAPIResponse(Response.Status.INTERNAL_SERVER_ERROR, reply.getMessage()));
//
//            return;
//        }
//        if (!reply.getMessage().isBlank())
//            logger.warn("Got JSONReply OK with message: {}", reply.getMessage());
//
//        String jsonResult = reply.getResult();
//        String objectID = reply.getObjectID();
//        Class<? extends Object> type = reply.getResultType();
//        boolean responseCompleted = false;
//        switch (info.getPath()) {
//            case ENDPOINT_PROCESS -> {
//                info.addResponse(objectID, objectID, jsonResult, type);
//                if (info.hasAllResponses()) {
//                    var response = new GenericWebAPIResponse("Returned endpoint-process", null);
//                    info.getCallback().triggerResponse(info.getOpID(), response);
//                    responseCompleted = true;
//                }
//            }
//            case ENABLE -> {
//                info.addResponse(objectID, objectID, jsonResult, type);
//                if (info.hasAllResponses()) {
//                    List<Event> events = this.getEnabledEvents();
//                    var response = new GenericWebAPIResponse("Returned enabled events", events);
//                    info.getCallback().triggerResponse(info.getOpID(), response);
//                    responseCompleted = true;
//                }
//            }
//            case EVENTS -> {
//                info.addResponse(objectID, objectID, jsonResult, type);
//                if (info.hasAllResponses()) {
//                    List<Event> events = this.getAllEvents();
//                    var response = new GenericWebAPIResponse("Returned all events", events);
//                    info.getCallback().triggerResponse(info.getOpID(), response);
//                    responseCompleted = true;
//                }
//            }
//        }
//
//        if (responseCompleted)
//            pendingRequests.remove(opID);
//    }

    // Callback for the Graph Runner
    @Override
    public Set<UserVal> uponSendRequest(UserVal requester, String eventId, UserSetVal receivers,
                                        Event.Marking marking, String uidExtension) {
        var neighbours = DummyMembershipLayer.instance()
                .resolveParticipants(receivers)
                .stream()
                .filter(n -> !n.user().equals(requester))
                .collect(Collectors.toSet());
        var reachable = new HashSet<MembershipLayer.Neighbour>();
        neighbours.forEach(neighbour -> {
            if (deliverMessage(neighbour, eventId, marking, requester, uidExtension)) {
                reachable.add(neighbour);
            }
        });
        // return neighbours.stream().map(MembershipLayer.Neighbour::user).collect(Collectors
        // .toSet());
        return reachable.stream().map(MembershipLayer.Neighbour::user).collect(Collectors.toSet());
    }


    // TODO some renaming
    // called from UI
    String onDisplayGraph() {
        return runner.toString();
    }

    // called from UI
    String onListEnabledEvents() {
        return runner.enabledEvents()
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
    }

    List<Event> getEnabledEvents() {
        return runner.enabledEvents();
    }

    List<Event> getAllEvents() {
        return List.of();
    }


    // TODO maybe return something to be printed
    // called from UI
    void onExecuteComputationEvent(String eventId) {
        logger.info("Executing Computation event '{}'", eventId);
        try {
            runner.executeComputationEvent(eventId);
        } catch (Exception e) {
            logger.error("Error executing Computation Event '{}': {}", eventId, e.getMessage());
            e.printStackTrace();
        }
    }

    // called from UI
    void onExecuteInputEvent(String eventId, Value inputValue) {
        logger.info("Executing Input event '{}' with input value {}", eventId, inputValue);
        try {
            runner.executeInputEvent(eventId, inputValue);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    void onExecuteInputEvent(String eventId) {
        logger.info("Executing (void) Input event '{}'", eventId);
        try {
            runner.executeInputEvent(eventId);
        } catch (Exception e) {
            logger.error("Error executing Input Event '{}': {}", eventId, e.getMessage());
            e.printStackTrace();
        }
    }

    // called from Babel's internal network after a remote event has been executed
    private void onExecuteReceiveEvent(GraphRunner runner, String eventId, Event.Marking marking,
                                       UserVal sender, String uidExtension) {
        logger.info("Executing Receive event '{}': received {}", eventId, marking);
        try {
            runner.onReceiveEvent(eventId, marking.value(), sender, uidExtension);
        } catch (Exception e) {
            logger.error("Error executing Receive Event '{}': {}", eventId, e.getMessage());
            e.printStackTrace();
        }
    }

    // (inner-process) DCR Protocol request to enact a Tx/RX
    private boolean deliverMessage(MembershipLayer.Neighbour receiver, String eventId,
                                   Event.Marking marking, UserVal user, String uidExtension) {
        try {
            String hostName = receiver.hostName();
            InetAddress targetAddr = InetAddress.getByName(hostName);
            Host destination = new Host(targetAddr, DEFAULT_PORT);
            var request = new AppRequest(eventId, marking, destination, user, uidExtension);
            sendRequest(request, DistributedDCRProtocol.PROTO_ID);
            return true;
        } catch (UnknownHostException e) {
            logger.warn("Unable to deliver message: {}", e.getMessage());
            return false;
        }
    }

    /* =======================
     * DCR Protocol handlers
     * ======================= */

    // handle incoming (inner-process) DCRProtocol request (Rx)
    private void uponReceiveDcrRequest(AppRequest appRequest, short sourceProtocol) {
        try {
            onExecuteReceiveEvent(runner, appRequest.getEventId(), appRequest.getMarking(),
                    appRequest.getSender(), appRequest.getIdExtensionToken());
        } catch (Exception e) {
            logger.error("Error reading command: {}", e.getMessage());
        }
    }

    /* =======================
     * Observer callback
     * ======================= */

    @Override
    public void onUpdate(List<StateUpdate> update) {
        logger.info("Observed state update");
    }

    /* =====================
     * WebService Handlers
     * ===================== */


    @Override
    protected void createAsync(String opUniqueID, Object o, WebAPICallback webAPICallback,
                               Optional<EndpointPath> optional) {

    }

    @Override
    protected void updateAsync(String s, Object o, WebAPICallback webAPICallback,
                               Optional<EndpointPath> optional) {
        if (optional.isEmpty())
            return;

//        TODO [revisit null initialization]
        GenericWebAPIResponse response = null;
        switch ((DCRGraphREST.DCREndpoints) optional.get()) {
            case COMPUTATION:
                this.onExecuteComputationEvent((String) o);

                response =  new GenericWebAPIResponse("Update computation event", o);
                break;
            case INPUT:
                var input = (InputRequest) o;
                response = new GenericWebAPIResponse("Update input event", o);
                this.onExecuteInputEvent(input.eventID(), input.inputValue());
                break;
            default:
                logger.info("Unexpected endpointPath call: {}", optional.get());
        }
        webAPICallback.triggerResponse(s,response);

    }

    @Override
    protected void readAsync(String opUniqueID, Object o, WebAPICallback webAPICallback,
                             Optional<EndpointPath> endpointPath) {
        if (endpointPath.isEmpty())
            return;
        var path = (DCRGraphREST.DCREndpoints) endpointPath.get();
        switch (path) {
            case ENDPOINT_PROCESS -> {
                var response = new GenericWebAPIResponse("Returned endpoint-process", null);
                webAPICallback.triggerResponse(opUniqueID, response);
            }
            case ENABLE -> {
                List<Event> events = this.getEnabledEvents();
                var response = new GenericWebAPIResponse("Returned enabled events", events);
                webAPICallback.triggerResponse(opUniqueID, response);
            }
            case EVENTS -> {
                List<Event> events = this.getAllEvents();
                var response = new GenericWebAPIResponse("Returned all events", events);
                webAPICallback.triggerResponse(opUniqueID, response);

            }
            default -> logger.info("Unexpected endpointPath call: {}", endpointPath.get());

        }
//        DCRAppRequest request = new DCRAppRequest(opUniqueID,webAPICallback,path, 1);
//        this.pendingRequests.put(opUniqueID, request);
    }

    @Override
    protected void deleteAsync(String s, Object o, WebAPICallback webAPICallback,
                               Optional<EndpointPath> optional) {
    }
}
