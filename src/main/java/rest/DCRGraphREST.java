package rest;

import dcr.common.data.values.Value;
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

import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
    public void getEvent(AsyncResponse ar, String eventId) {
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
        this.sendRequest(WebServiceOperation.UPDATE, eventId,DCREndpoints.COMPUTATION, ar);
    }

    @Override
    public void executeInputEvent(@Suspended AsyncResponse ar, String eventId, Value input) {
        logger.info("\n\n\n UPDATE Input event");
        this.sendRequest(WebServiceOperation.UPDATE, new InputRequest(eventId,input),DCREndpoints.INPUT, ar);
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
                sendStatusResponse(ar,genericResponse.getStatusCode(),genericResponse.getMessage());
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
