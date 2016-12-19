package kz.gcs.maps.client.rpcs;

import com.vaadin.shared.communication.ServerRpc;
import kz.gcs.maps.client.base.LatLon;

/**
 * @author Igor Korotkov (igor@ikorotkov.com)
 */
public interface MapInitRpc extends ServerRpc {
    public void init(LatLon center, int zoom, LatLon boundsNE, LatLon boundsSW);
}
