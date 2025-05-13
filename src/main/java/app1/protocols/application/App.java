package app1.protocols.application;

import app1.DCRApp;
import app1.Endpoint;
import app1.membership.DummyMembershipLayer;
import app1.presentation.endpoint.EndpointDTO;
import app1.presentation.mappers.EndpointMapper;
import app1.protocols.dcr.DCRProtocol;
import app1.protocols.dcr.requests.DcrRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import dcr1.common.Record;
import dcr1.common.data.computation.*;
import dcr1.common.data.types.BooleanType;
import dcr1.common.data.types.IntegerType;
import dcr1.common.data.types.StringType;
import dcr1.common.data.types.Type;
import dcr1.common.data.values.*;
import dcr1.common.events.Event;
import dcr1.common.events.userset.values.UserSetVal;
import dcr1.common.events.userset.values.UserVal;
import dcr1.runtime.GraphRunner;
import dcr1.runtime.communication.CommunicationLayer;
import dcr1.runtime.communication.MembershipLayer;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.unl.di.novasys.babel.webservices.WebAPICallback;
import pt.unl.di.novasys.babel.webservices.application.GenericWebServiceProtocol;
import pt.unl.di.novasys.babel.webservices.utils.EndpointPath;
import pt.unl.di.novasys.babel.webservices.utils.GenericWebAPIResponse;
import pt.unl.fct.di.novasys.babel.exceptions.HandlerRegistrationException;
import pt.unl.fct.di.novasys.network.data.Host;
import rest.DCRGraphREST.DCREndpoints;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

// Singleton
public class App {
    //     extends GenericWebServiceProtocol
    //     implements CommunicationLayer {
    //
    // private static final Logger logger = LogManager.getLogger(App.class);
    //
    // private static final String PROTOCOL_NAME = "dcrApplication";
    // private static final short PROTOCOL_ID = 1001;
    // private static final String ROLE_RESOURCE_PROP = "role";
    //
    // // TODO: read this from properties
    // // port being used for TCP communication between neighbors (hopefully something other
    // // than TCP will be used in the future)
    // protected static final int DEFAULT_PORT = 9000;
    //
    // private static final class LazyHolder {
    //     static final App INSTANCE = new App();
    // }
    //
    // public static App getInstance() {
    //     return LazyHolder.INSTANCE;
    // }
    //
    // // attempt to fetch and decode json-encoded endpoint resource
    // private static Endpoint loadEndpoint(Properties properties) {
    //     try (InputStream in = DCRApp.class.getResourceAsStream(
    //             String.format("/%s", properties.getProperty(ROLE_RESOURCE_PROP)))) {
    //         assert in != null;
    //         var jsonEncodedEndpoint = new String(in.readAllBytes(), StandardCharsets.UTF_8);
    //         ObjectMapper objectMapper = new ObjectMapper();
    //         objectMapper.registerModule(new Jdk8Module());
    //         var deserializedEndpoint =
    //                 objectMapper.readValue(jsonEncodedEndpoint, EndpointDTO.class);
    //         return EndpointMapper.mapEndpoint(deserializedEndpoint);
    //     } catch (Exception e) {
    //         logger.error(e);
    //         throw new InternalError(
    //                 String.format("Failed to load endpoint resource: %s\n%s", ROLE_RESOURCE_PROP,
    //                         e.getMessage()));
    //     }
    // }
    //
    //
    // private static UserVal instantiateSelf(Properties props, Endpoint.Role roleDecl) {
    //     return UserVal.of(roleDecl.roleName(), Record.ofEntries(roleDecl.params()
    //             .stream()
    //             .map(param -> fetchSelfParamField(props, param.name(), param.value()))
    //             .collect(Collectors.toMap(Record.Field::name, Record.Field::value))));
    // }
    //
    // //
    // // private static RecordVal instantiateSelf(Properties props, Endpoint.Role roleDecl) {
    // //     return RecordVal.of(Record.ofEntries(roleDecl.params()
    // //             .stream()
    // //             .map(param -> fetchSelfParamField(props, param.name(), param.value()))
    // //             .collect(Collectors.toMap(Record.Field::name, Record.Field::value))));
    // // }
    //
    // private static Record.Field<Value> fetchSelfParamField(Properties props,
    //         String key, Type type) {
    //     var prop = props.getProperty(key);
    //     return Record.Field.of(key, switch (type) {
    //         case BooleanType ignored -> BoolVal.of(Boolean.parseBoolean(prop));
    //         case IntegerType ignored -> IntVal.of(Integer.parseInt(prop));
    //         case StringType ignored -> StringVal.of(prop);
    //         default -> throw new IllegalStateException("Unexpected value for role param: " + type);
    //     });
    // }
    //
    // // private static RecordExpr instantiateSelf(Properties props, Endpoint.Role roleDecl) {
    // //     return RecordExpr.of(Record.ofEntries(roleDecl.params()
    // //             .stream()
    // //             .map(param -> fetchSelfParamField(props, param.name(), param.value()))
    // //             .collect(Collectors.toMap(Record.Field::name, Record.Field::value))));
    // // }
    //
    // // private static Record.Field<ComputationExpression> fetchSelfParamField(Properties props,
    // //         String key, Type type) {
    // //     var prop = props.getProperty(key);
    // //     return Record.Field.of(key, switch (type) {
    // //         case BooleanType ignored -> BoolLiteral.of(Boolean.parseBoolean(prop));
    // //         case IntegerType ignored -> IntLiteral.of(Integer.parseInt(prop));
    // //         case StringType ignored -> StringLiteral.of(prop);
    // //         default -> throw new IllegalStateException("Unexpected value for role param: " + type);
    // //     });
    // // }
    //
    // // this should be constant
    // private String jsonEncodedEndpoint;
    // private boolean initialized;
    // private GraphRunner runner;
    //
    // private App() {
    //     super(PROTOCOL_NAME, PROTOCOL_ID);
    //     this.initialized = false;
    // }
    //
    //
    // /* ================
    //  * Babel Handlers
    //  * ================ */
    //
    // @Override
    // public synchronized void init(Properties properties)
    //         throws HandlerRegistrationException, IOException {
    //     if (initialized) {return;}
    //
    //     try (InputStream in = DCRApp.class.getResourceAsStream(
    //             String.format("/%s", properties.getProperty(ROLE_RESOURCE_PROP)))) {
    //         assert in != null;
    //         var jsonEncodedEndpoint = new String(in.readAllBytes(), StandardCharsets.UTF_8);
    //
    //         var endpoint = loadEndpoint(properties);
    //         var graphModel = endpoint.graphModel();
    //         var self = instantiateSelf(properties, endpoint.role());
    //
    //         this.jsonEncodedEndpoint = jsonEncodedEndpoint;
    //         this.runner = new GraphRunner(self, this);
    //         this.runner.init(graphModel);
    //     } catch (Exception e) {
    //         logger.error(e);
    //         throw new InternalError(
    //                 String.format("Failed to load endpoint resource: %s\n%s", ROLE_RESOURCE_PROP,
    //                         e.getMessage()));
    //     }
    //
    //
    //     // TODO will depend on the protocol... register handlers, send initial requests,...
    //     this.initialized = true;
    // }
    //
    //
    // @Override
    // public Set<UserVal> uponSendRequest(UserVal sender, String eventId, UserSetVal receivers,
    //         Event.Marking marking, String uidExtension) {
    //     var neighbours = DummyMembershipLayer.instance().resolveParticipants(receivers);
    //     neighbours.forEach(
    //             receiver -> sendMessage(receiver, eventId, marking, sender, uidExtension));
    //     return neighbours.stream().map(MembershipLayer.Neighbour::user).collect(Collectors.toSet());
    // }
    //
    //
    // /* =====================
    //  * Application Handlers
    //  * ===================== */
    //
    //
    // @Override
    // protected void createAsync(String s, Object o, WebAPICallback webAPICallback,
    //         Optional<EndpointPath> optional) {
    //
    // }
    //
    // @Override
    // protected void updateAsync(String s, Object o, WebAPICallback webAPICallback,
    //         Optional<EndpointPath> optional) {
    //
    // }
    //
    // @Override
    // protected void readAsync(String opUID, Object value, WebAPICallback callback,
    //         Optional<EndpointPath> endpointPath) {
    //     if (endpointPath.isEmpty()) {
    //         return;
    //     }
    //     int expectedResponses = -1;
    //     DCREndpoints path = (DCREndpoints) endpointPath.get();
    //     switch (path) {
    //         case ENDPOINT_PROCESS:
    //             expectedResponses = 1;
    //         default:
    //             break;
    //     }
    //     if (expectedResponses == -1) {
    //         GenericWebAPIResponse genericResponse =
    //                 new GenericWebAPIResponse(Response.Status.NOT_FOUND, "Something went wrong!");
    //         callback.triggerResponse(opUID, genericResponse);
    //         return;
    //     }
    //     callback.triggerResponse(opUID, new GenericWebAPIResponse(jsonEncodedEndpoint));
    // }
    //
    // @Override
    // protected void deleteAsync(String s, Object o, WebAPICallback webAPICallback,
    //         Optional<EndpointPath> optional) {
    //
    // }
    //
    //
    // private void sendMessage(MembershipLayer.Neighbour receiver, String eventId,
    //         Event.Marking marking, UserVal user, String uidExtension) {
    //     try {
    //         String hostName = receiver.hostName();
    //         InetAddress targetAddr = InetAddress.getByName(hostName);
    //         Host destination = new Host(targetAddr, DEFAULT_PORT);
    //         // request = new DcrRequest(eventId, JSON.encode(marking), destination);
    //         var request = new DcrRequest(eventId, marking, destination, user, uidExtension);
    //         logger.info("Sending message to receiver {}...", receiver);
    //         sendRequest(request, DCRProtocol.PROTO_ID);
    //         logger.info("  Message Sent.");
    //     } catch (Exception e1) {
    //         logger.warn("Error executing event: {}", e1.getMessage());
    //     }
    // }

}
