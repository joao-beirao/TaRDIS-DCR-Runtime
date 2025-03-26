package dcr1.runtime.events;

import dcr1.common.events.ReceiveEvent;
import dcr1.common.data.types.Type;

public interface ReceiveEventInstance<T extends Type>
    extends RemotelyInitiatedEventInstance<T>, ReceiveEvent<T> {
}

