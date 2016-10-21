package kz.gcs.maps.client.rpcs;

import com.vaadin.shared.communication.ServerRpc;

/**
 * An RPC from client to server that is called when a marker has been clicked in
 * Google Maps.
 */
public interface MarkerClickedRpc extends ServerRpc {
    void markerClicked(long markerId);
}
