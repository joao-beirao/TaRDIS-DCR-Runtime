package rest;

import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.unl.di.novasys.babel.webservices.application.GenericWebServiceProtocol.WebServiceOperation;
import pt.unl.di.novasys.babel.webservices.rest.GenericREST;
import pt.unl.di.novasys.babel.webservices.utils.EndpointPath;
import pt.unl.di.novasys.babel.webservices.utils.GenericWebAPIResponse;
import pt.unl.di.novasys.babel.webservices.utils.PendingResponse;

// TODO [credit where credit is due] - add pointers to actual authors (straightforward adaptation)

@Singleton
@Path(DCRGraphREST.PATH)
public class DCRGraphREST
        extends GenericREST {

    public static final String PATH = "/dcr";
    private static final String ERROR_MESSAGE = "Internal server error!";
    private static final Logger logger = LogManager.getLogger(DCRGraphREST.class);

    public enum DCREndpoints
            implements EndpointPath {
        ENDPOINT_PROCESS("endpoint_process");

        private final String endpointPath;

        DCREndpoints(String endpointPath) {this.endpointPath = endpointPath;}

        @Override
        public String getPath() {return endpointPath;}
    }

    public DCRGraphREST() {
        super();
    }

    // dummy
    @GET
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public Response endpointProcess() {
        logger.info("\n\n\nEndpoint process requested");

        return Response.status(Response.Status.OK)
                .entity("all good")
                .type(MediaType.TEXT_PLAIN)
                .build();
    }

    @GET
    @Path("/endpoint")
    @Produces(MediaType.APPLICATION_JSON)
    public void endpointProcess(@Suspended final AsyncResponse ar) {
        this.sendRequest(WebServiceOperation.READ, DCREndpoints.ENDPOINT_PROCESS, ar);
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
                }
                else {sendResponse(ar, Response.Status.OK, genericResponse);}
        }
    }

    private void sendResponse(AsyncResponse ar, Response.Status statusCode, Object value) {
        Response response = Response.status(statusCode).entity(value).build();
        ar.resume(response);
    }


    private void sendStatusResponse(AsyncResponse ar, Response.Status statusCode, String message) {
        Response response = Response.status(statusCode).entity(message).build();
        ar.resume(response);
    }

}
