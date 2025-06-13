
 package websocket;

import java.io.IOException;

import jakarta.websocket.EncodeException;

import jakarta.websocket.Session;

import jakarta.websocket.server.ServerEndpoint;

import pt.unl.di.novasys.babel.webservices.application.GenericWebServiceProtocol;

import pt.unl.di.novasys.babel.webservices.utils.GenericWebAPIResponse;

import pt.unl.di.novasys.babel.webservices.websocket.GenericWebSocket;

import pt.unl.di.novasys.babel.webservices.websocket.utils.JSONMessageEncoder;

//@ServerEndpoint(value = DCRWS.PATH, encoders = JSONMessageEncoder.class)
@ServerEndpoint(value = DCRWS.PATH, encoders = JacksonEncoder.class)

public class DCRWS extends GenericWebSocket {

    public static final String PATH = "/dcr";

    public DCRWS(GenericWebServiceProtocol babelApp) {

        super(babelApp);

    }

    @Override

    public void onMessage(Session session, String message) throws IOException {

        // Not implemented in this app.

    }

    @Override

    public void sendMessage(Object value) {

        try {

            session.getBasicRemote().sendObject(value);

        } catch (IOException | EncodeException e) {

            e.printStackTrace();

        }

    }

    @Override

    public void triggerResponse(String opUniqueID, GenericWebAPIResponse response) {

        // Not implemented in this app.

    }

}

