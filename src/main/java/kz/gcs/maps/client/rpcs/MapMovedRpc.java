package kz.gcs.maps.client.rpcs;

import com.vaadin.shared.communication.ServerRpc;
import kz.gcs.maps.client.LatLon;

/**
 * An RPC from client to server that is called when the user has moved the map.
 */
public interface MapMovedRpc extends ServerRpc {
    void mapMoved(int zoomLevel, LatLon center, LatLon boundsNE,
                  LatLon boundsSW);
}
