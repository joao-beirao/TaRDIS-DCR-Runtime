package pingpong.requests;
import java.util.Objects;

import dcr.EventMarking;
import pt.unl.fct.di.novasys.babel.generic.ProtoRequest;
import pt.unl.fct.di.novasys.network.data.Host;

public class DcrRequest extends ProtoRequest {

    public static final short REQUEST_ID = 1;
    private final Host destination;

    private final String eventId;
    private final EventMarking marking;

    public DcrRequest(String eventId, EventMarking marking, Host destination) {
        super(REQUEST_ID);
        this.destination = destination;
        this.eventId = eventId;
        this.marking = Objects.requireNonNull(marking);
    }

    public Host getDestination() {
        return destination;
    }

    public String getEventId() {
        return this.eventId;
    }

    public EventMarking getMarking() {
        return this.marking;
    }

    /*
     * public DcrReply produceReply(String receiver, long rtt) {
     * return new DcrReply(message, receiver, destination, rtt);
     * }
     */

}
