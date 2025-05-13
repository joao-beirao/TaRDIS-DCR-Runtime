package dcr1.runtime.communication;

import dcr1.common.events.Event;
import dcr1.common.events.userset.values.UserSetVal;
import dcr1.common.events.userset.values.UserVal;

import java.util.Set;

public interface CommunicationLayer {
    public Set<UserVal> uponSendRequest(UserVal requester, String eventId, UserSetVal receivers,
            Event.Marking marking,
            String uidExtension);
}
