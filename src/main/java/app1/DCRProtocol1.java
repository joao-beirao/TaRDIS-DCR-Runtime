package app1;

// import static pt.unl.fct.di.novasys.babel.core.GenericProtocol.logger;

import app1.membership.DummyMembershipLayer;
import app1.protocols.dcr.requests.DcrReply;
import app1.protocols.dcr.requests.DcrRequest;
import app1.protocols.pingpong.PingPongProtocol;
import dcr1.common.data.values.Value;
import dcr1.common.events.Event;
import dcr1.common.events.userset.values.UserSetVal;
import dcr1.common.events.userset.values.UserVal;
import dcr1.runtime.ExecutionResult;
import dcr1.runtime.GraphRunner;
import dcr1.runtime.communication.CommunicationLayer;
import dcr1.runtime.communication.MembershipLayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.unl.fct.di.novasys.babel.core.GenericProtocol;
import pt.unl.fct.di.novasys.babel.exceptions.HandlerRegistrationException;
import pt.unl.fct.di.novasys.network.data.Host;

import java.io.IOException;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class DCRProtocol1
        extends GenericProtocol
        implements CommunicationLayer {

    public static final short PROTO_ID = 51; // unique protocol id
    public static final String PROTOCOL_NAME = "DCRApp";

    protected static final int DEFAULT_PORT = 9000; // default port to listen on
    protected static final Logger logger = LogManager.getLogger(DCRProtocol1.class);

    protected GraphRunner runner;

    public DCRProtocol1(Properties props) {
        super(PROTOCOL_NAME, PROTO_ID);

    }

    public void init(Properties properties) throws HandlerRegistrationException, IOException {

        logger.info("Initializing DCRProtocol1 - registering request handlers");
        // register protocol handlers
        // --- none at this point ----
        // register request handlers
        registerRequestHandler(DcrRequest.REQUEST_ID, this::uponReceiveDcrRequest);
        // register reply handler
        registerReplyHandler(DcrReply.REPLY_ID, this::onPongReply);
        // start CLI
        // cmdLineRunner.processCommands();
    }

    // Callback for the Graph Runner
    @Override
    public Set<UserVal> uponSendRequest(UserVal requester, String eventId, UserSetVal receivers,
            Event.Marking marking,
            String uidExtension) {
        logger.info("on uponSendRequest: from" + requester);
        var neighbours =
                DummyMembershipLayer.instance()
                        .resolveParticipants(receivers)
                        .stream()
                        .filter(n -> !n.user().equals(requester))
                        .collect(
                                Collectors.toSet());
        neighbours.forEach(
                neighbour -> sendMessage(neighbour, eventId, marking, requester, uidExtension));
        return neighbours.stream()
                .map(MembershipLayer.Neighbour::user)
                .collect(Collectors.toSet());
    }


    // TODO some renaming
    // called from UI
    String onDisplayGraph() {
        return runner.toString();
    }

    // called from UI
    String onListEnabledEvents() {
        return runner.lookupEnabledEvents()
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
    }

    // TODO maybe return something to be printed
    // called from UI
    void onExecuteComputationEvent(String eventId) {
        logger.info("Executing ComputationAction '{}'...", eventId);
        try {
            ExecutionResult result = runner.executeComputationEvent(eventId);
            logger.info("ComputationAction executed. Event marking updated to {}",
                    result.getMarking());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    // called from UI
    void onExecuteInputEvent(String eventId, Value inputValue) {
        logger.info("Executing input action '{}' with input value {}", eventId, inputValue);
        try {
            runner.executeInputEvent(eventId, inputValue);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    void onExecuteInputEvent(String eventId) {
        logger.info("Executing empty input action '{}'", eventId);
        try {
            runner.executeInputEvent(eventId);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    // called from Babel's internal network after a remote event has been executed
    private void onExecuteReceiveEvent(GraphRunner runner, String eventId,
            Event.Marking marking, UserVal sender, String uidExtension) {
        logger.info("Executing receive operation '{}': received {}", eventId, marking);
        try {
            runner.onReceiveEvent(eventId, marking.value(), sender, uidExtension);
            // TODO pass on something to be printed
            // cmdLineRunner.onReceiveEvent();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }

    // registered handler for incoming DcrRequests - at this point, this can only be a request to
    private void uponReceiveDcrRequest(DcrRequest dcrRequest, short sourceProtocol) {
        logger.info("@App: DCR Request received - execute Rx event {} with marking {}",
                dcrRequest.getEventId(), dcrRequest.getMarking());
        try {
            onExecuteReceiveEvent(runner, dcrRequest.getEventId(), dcrRequest.getMarking(),
                    dcrRequest.getSender(), dcrRequest.getIdExtensionToken());
        } catch (Exception e) {
            logger.error("Error reading command: {}", e.getMessage());
        }
    }

    // send a message to another Babel DCR node
    private void sendMessage(MembershipLayer.Neighbour receiver, String eventId,
            Event.Marking marking, UserVal user, String uidExtension) {
        try {
            String hostName = receiver.hostName();
            InetAddress targetAddr = InetAddress.getByName(hostName);
            logger.info("Sending message to target {}...", targetAddr);
            Host destination = new Host(targetAddr, DEFAULT_PORT);
            // request = new DcrRequest(eventId, JSON.encode(marking), destination);
            var request = new DcrRequest(eventId, marking, destination, user, uidExtension);
            logger.info("Sending message to receiver {}...", receiver);
            sendRequest(request, PingPongProtocol.PROTO_ID);
            logger.info("  Message Sent.");
        } catch (Exception e1) {
            logger.warn("Error executing event: {}", e1.getMessage());
        }
    }


    // ========================================================================
    // TODO likely safe to discard after this point
    // ========================================================================

    // // TODO safe remove ?
    // /**
    //  * Handle Pong Reply
    //  *
    //  * @param pongReply
    //  *         pong reply message
    //  * @param sourceProto
    //  *         source protocol
    //  */
    private void onPongReply(DcrReply pongReply, short sourceProto) {
        logger.info("Received reply from {} in {} ms", pongReply.getDestination(),
                pongReply.getRTT());
        // received++;
        // if (received == 1) {
        //     readSystemIn();
        // }
    }

    // TODO maybe we should know the event's <T>ype here
    private static Iterable<String> translate_participants(Iterable<String> participants) {
        List<String> refactored = new LinkedList<>();
        for (String participant : participants) {
            refactored.add(participant.replace("(", "_").replace(")", ""));
        }
        return refactored;
    }

}