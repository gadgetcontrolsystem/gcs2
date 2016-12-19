package kz.gcs.maps.client.rpcs.click;

import com.vaadin.shared.communication.ServerRpc;
import kz.gcs.maps.client.base.LatLon;

/**
 * An RPC from client to server that is called when a marker has been clicked in
 * Google Maps.
 * 
 * @author Ivano Selvaggi <ivoselva@gmail.com>
 * 
 */
public interface MapClickedRpc extends ServerRpc {
    public void mapClicked(LatLon position);
}
