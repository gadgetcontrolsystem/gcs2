package kz.gcs.maps.client.rpcs.centerchange;

import com.vaadin.shared.communication.ServerRpc;
import kz.gcs.maps.client.base.LatLon;

/**
 * @author korotkov
 * @version $Id$
 */
public interface CircleCenterChangeRpc extends ServerRpc {
    void centerChanged(long circleId, LatLon newCenter);
}
