package kz.gcs.maps.client.rpcs;

import com.vaadin.shared.communication.ServerRpc;
import kz.gcs.maps.client.services.DirectionsResult;
import kz.gcs.maps.client.services.DirectionsStatus;

/**
 * @author Igor Korotkov (igor@ikorotkov.com)
 */
public interface HandleDirectionsResultRpc extends ServerRpc {
    void handle(DirectionsResult result, DirectionsStatus status, long directionsRequestId);
}
