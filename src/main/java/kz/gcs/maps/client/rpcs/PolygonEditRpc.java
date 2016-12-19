package kz.gcs.maps.client.rpcs;

import com.vaadin.shared.communication.ServerRpc;
import kz.gcs.maps.client.base.LatLon;
import kz.gcs.maps.client.events.PolygonEditListener;

/**
 * @author Igor Korotkov (igor@ikorotkov.com)

 */
public interface PolygonEditRpc extends ServerRpc {
    public void polygonEdited(long polygonId, PolygonEditListener.ActionType
            actionType, int idx, LatLon latLon);
}
