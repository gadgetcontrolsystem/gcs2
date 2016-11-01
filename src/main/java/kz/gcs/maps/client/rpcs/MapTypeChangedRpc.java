package kz.gcs.maps.client.rpcs;

import com.vaadin.shared.communication.ServerRpc;

/**
 * An RPC from client to server that is called when the map type has been changed by the user.
 */
public interface MapTypeChangedRpc extends ServerRpc {
    void mapTypeChanged(String mapTypeId);
}
