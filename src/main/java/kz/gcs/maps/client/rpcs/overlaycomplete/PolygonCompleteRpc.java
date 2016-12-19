package kz.gcs.maps.client.rpcs.overlaycomplete;

import com.vaadin.shared.communication.ServerRpc;
import kz.gcs.maps.client.overlays.GoogleMapPolygon;

/**
 * @author Igor Korotkov (igor@ikorotkov.com)

 */
public interface PolygonCompleteRpc extends ServerRpc {
    void polygonComplete(GoogleMapPolygon polygon);
}
