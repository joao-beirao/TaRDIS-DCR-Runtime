package rest;

import app.presentation.endpoint.data.values.ValueDTO;
import dcr.common.Record;
import dcr.common.data.values.*;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.unl.di.novasys.babel.webservices.application.GenericWebServiceProtocol;
import pt.unl.di.novasys.babel.webservices.application.GenericWebServiceProtocol.WebServiceOperation;
import pt.unl.di.novasys.babel.webservices.rest.GenericREST;
import pt.unl.di.novasys.babel.webservices.utils.EndpointPath;
import pt.unl.di.novasys.babel.webservices.utils.GenericWebAPIResponse;
import pt.unl.di.novasys.babel.webservices.utils.PendingResponse;
import rest.request.InputRequest;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static app.presentation.mappers.EndpointMapper.fromValueDTO;

// TODO [credit where credit is due] - add pointers to actual authors (straightforward adaptation)


@Singleton
@Path(DCRGraphAPI.PATH)
public class DCRGraphREST
        extends GenericREST implements DCRGraphAPI {

    private static final String ERROR_MESSAGE = "Internal server error!";
    private static final Logger logger = LogManager.getLogger(DCRGraphREST.class);

    public enum DCREndpoints
            implements EndpointPath {
        ENDPOINT_PROCESS("endpoint_process"),
        EVENT("eventId"),
        EVENTS("events"),
        ENABLE("enable"),
        COMPUTATION("computation"),
        INPUT("input");


        private final String endpointPath;

        DCREndpoints(String endpointPath) {
            this.endpointPath = endpointPath;
        }

        @Override
        public String getPath() {
            return endpointPath;
        }
    }

    public DCRGraphREST() {
        super();
    }

    @Override
    public Response endpointProcess() {
        logger.info("\n\n\nEndpoint process requested");

        return Response.status(Response.Status.OK)
                .entity("all good")
                .type(MediaType.TEXT_PLAIN)
                .build();
    }

    @Override
    public void endpointProcess(@Suspended final AsyncResponse ar) {
        this.sendRequest(WebServiceOperation.READ, DCREndpoints.ENDPOINT_PROCESS, ar);
    }

    @Override
    public void getEvents(@Suspended AsyncResponse ar) {
        logger.info("\n\n\nGet events requested");
        this.sendRequest(WebServiceOperation.READ, DCREndpoints.EVENTS, ar);
    }

    @Override
    public void getEvent(@Suspended     AsyncResponse ar, String eventId) {
        logger.info("\n\n\nGet event");
        this.sendRequest(WebServiceOperation.READ, eventId,DCREndpoints.EVENT, ar);
    }

    @Override
    public void getEnableEvents(@Suspended AsyncResponse ar) {
        logger.info("\n\n\nGet enabled events");
        this.sendRequest(WebServiceOperation.READ, DCREndpoints.ENABLE, ar);
    }

    @Override
    public void executeComputationEvent(@Suspended AsyncResponse ar, String eventId) {
        logger.info("\n\n\n UPDATE computation event");
        ar.setTimeout(60, TimeUnit.SECONDS);
        this.sendRequest(WebServiceOperation.UPDATE, eventId,DCREndpoints.COMPUTATION, ar);
    }
//
//    private static RecordVal parseRecordVal(String content) {
//        if (content.isEmpty())
//            throw new IllegalArgumentException("Expecting record fields: empty record not        supported");
//
//        var builder = new Record.Builder<Value>();
//        String rest = content;
//        while (!rest.isEmpty()) {
//            var field_end_pos = -1;
//            var next_field_pos = -1;
//
//            var field_assign_pos = rest.indexOf(":");
//
//            if (rest.charAt(field_assign_pos + 1) == '{') {
//                // record field value follows - use '}' do detect end of record
//                field_end_pos = rest.indexOf("}");
//                if (field_end_pos < rest.length() - 1 && rest.charAt(field_end_pos + 1) == ';') {
//                    // one more field after this one
//                    next_field_pos = field_end_pos;
//
//                } else {
//                    // this is the last field
//                    next_field_pos = rest.length();
//                }
//                builder.addField(parseRecordFieldVal(rest.substring(0, field_end_pos + 1)));
//            } else {
//                // primitive field value (not record)
//                next_field_pos = rest.indexOf(";");
//                if (next_field_pos == -1) {
//                    // this is the last field
//                    field_end_pos = rest.length();
//                    next_field_pos = rest.length();
//                } else {
//                    // one more field after this one
//                    field_end_pos = next_field_pos;
//                    ++next_field_pos;
//                }
//                builder.addField(parseRecordFieldVal(rest.substring(0, field_end_pos)));
//            }
//            rest = rest.substring(next_field_pos);
//        }
//        return RecordVal.of(builder.build());
//    }
//    private static Value parseInputVal(String input) {
//        input = input.trim();
//        // TODO handle empty input values
//        if (input.isEmpty()) {
//            throw new IllegalArgumentException(
//                    "Not implemented: empty input value not allowed at this point");
//        }
//        if (input.startsWith("{") && input.endsWith("}")) {
//            return parseRecordVal(input.substring(1, input.length() - 1).trim());
//        }
//        if (input.startsWith("'") && input.endsWith("'")) {
//            String string_val = input.substring(1, input.length() - 1);
//            return StringVal.of(input.substring(1, input.length() - 1));
//        }
//        // Boolean
//        try {
//            return IntVal.of(Integer.parseInt(input));
//        } catch (NumberFormatException e) {
//            throw new IllegalArgumentException("Unable to parse input value");
//        }
//    }
//    private static Record.Field<Value> parseRecordFieldVal(String field) {
//        var split_pos = field.indexOf(":");
//        var name = field.substring(0, split_pos);
//        var valueAsString = field.substring(split_pos + 1);
//        Value val = parseInputVal(valueAsString);
//        return new Record.Field<>(name, val);
//    }
    @Override
    public void executeInputEvent(@Suspended AsyncResponse ar, String eventId, InputRequest input) {
        logger.info("\n\n\n UPDATE Input event");
        ar.setTimeout(60, TimeUnit.SECONDS);
        this.sendRequest(WebServiceOperation.UPDATE, input,DCREndpoints.INPUT, ar);
    }

    @Override
    public void triggerResponse(String opUID, GenericWebAPIResponse genericResponse) {
        PendingResponse pendingResponse = super.removePendingResponse(opUID);
        var restEndpoint = (DCREndpoints) pendingResponse.getRestEnpoint();
        var ar = pendingResponse.getAr();
        switch (restEndpoint) {
            case ENDPOINT_PROCESS:
                if (genericResponse == null) {
                    sendStatusResponse(ar, Response.Status.INTERNAL_SERVER_ERROR, ERROR_MESSAGE);
                } else {
                    sendResponse(ar, genericResponse);
                }
                break;
            case EVENTS:
            case ENABLE:
                if (genericResponse == null) {
                    sendStatusResponse(ar, Response.Status.INTERNAL_SERVER_ERROR, ERROR_MESSAGE);
                } else {
                    sendResponse(ar,genericResponse.getValue());
                }
                break;
            case COMPUTATION:
            case INPUT:
                if (genericResponse == null) {
                    sendStatusResponse(ar, Response.Status.INTERNAL_SERVER_ERROR, ERROR_MESSAGE);
                }
                else {
                    sendStatusResponse(ar, genericResponse.getStatusCode(), genericResponse.getMessage());
                }
                break;
            default:
                break;
        }
    }

    private void sendResponse(AsyncResponse ar, Object value) {
        Response response = Response.status(Response.Status.OK).entity(value).build();
        ar.resume(response);
    }


    private void sendStatusResponse(AsyncResponse ar, Response.Status statusCode, String message) {
        Response response = Response.status(statusCode).entity(message).build();
        ar.resume(response);
    }

}
