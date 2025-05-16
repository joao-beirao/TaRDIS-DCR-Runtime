package protocols.application.requests;

import pt.unl.fct.di.novasys.babel.generic.ProtoReply;
import pt.unl.fct.di.novasys.network.data.Host;

public class AppReply
        extends ProtoReply {

    public static final short REPLY_ID = 2;
    private final String message;// carries executed event label
    private final String receiver;// carries receiver-target name
    private final Host destination;
    private final long rtt;

    public AppReply(String message, String receiver, Host destination, long rtt) {
        super(REPLY_ID);
        // contains executed event
        this.message = message;
        this.receiver = receiver;
        this.destination = destination;
        this.rtt = rtt;
    }
    public String getMessage() {
        return message;
    }

    public Host getDestination() {
        return destination;
    }

    public String getReceiver() {
        return receiver;
    }

    public long getRTT() {
        return rtt;
    }

}
