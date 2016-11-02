package kz.gcs.maps.client.rpcs;

import com.vaadin.shared.communication.ServerRpc;
import kz.gcs.maps.client.LatLon;

/**
 * An RPC from client to server that is called when a marker has been dragged in
 * Google Maps.
 */
public interface MarkerDraggedRpc extends ServerRpc {
    void markerDragged(long markerId, LatLon newPosition);
}
