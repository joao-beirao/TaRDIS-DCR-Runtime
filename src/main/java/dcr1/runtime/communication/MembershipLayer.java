package dcr1.runtime.communication;

import dcr1.common.events.userset.values.UserVal;
import pt.unl.fct.di.novasys.network.data.Host;

public interface MembershipLayer {

    interface Neighbour {
        UserVal user();

        Host host();
    }

    void onNeighborUp(Neighbour neighbourUp);

    void onNeighborDown(Neighbour neighbourDown);


}
