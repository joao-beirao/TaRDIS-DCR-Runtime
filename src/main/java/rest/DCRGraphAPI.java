package rest;

import dcr.common.data.values.Value;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.unl.di.novasys.babel.webservices.utils.EndpointPath;

import javax.print.attribute.standard.Media;


public interface DCRGraphAPI {
    public static final String PATH = "/dcr";

    public static final String EVENT_ID = "eventId";
    public static final String EVENTS = "/events";
    public static final String ENABLE = "enable";
    public static final String COMPUTATION = "computation";
    public static final String INPUT = "input";
    @GET
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    Response endpointProcess();


    @GET
    @Path("/endpoint")
    @Produces(MediaType.TEXT_PLAIN)
    void endpointProcess(@Suspended final AsyncResponse ar);

    @GET
    @Path(EVENTS)
    @Produces(MediaType.APPLICATION_JSON)
    void getEvents(@Suspended AsyncResponse ar);


    @GET
    @Path(EVENTS + "/{" + EVENT_ID +"}")
    @Produces(MediaType.APPLICATION_JSON)
    void getEvent(@Suspended AsyncResponse ar, @PathParam(EVENT_ID) String eventId);


    @GET
    @Path(EVENTS + "/"+ENABLE)
    @Produces(MediaType.APPLICATION_JSON)
    void getEnableEvents(@Suspended AsyncResponse ar);

    @PUT
    @Path(EVENTS+ "/" + COMPUTATION + "/{"+ EVENT_ID + "}")
    @Produces (MediaType.APPLICATION_JSON)
    void executeComputationEvent(@Suspended AsyncResponse ar, @PathParam(EVENT_ID) String eventId);


    @PUT
    @Path(EVENTS+ "/" + INPUT + "/{"+ EVENT_ID + "}")
    @Consumes( MediaType.APPLICATION_JSON)
    @Produces (MediaType.APPLICATION_JSON)
    void executeInputEvent(@Suspended AsyncResponse ar, @PathParam(EVENT_ID) String eventId,Value input);


}
