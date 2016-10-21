package kz.gcs.maps.client.rpcs;

import com.vaadin.shared.communication.ServerRpc;
import kz.gcs.maps.client.LatLon;

/**
 * An RPC from client to server that is called when a marker has been clicked in
 * Google Maps.
 */
public interface MapClickedRpc extends ServerRpc {
    void mapClicked(LatLon position);
}
