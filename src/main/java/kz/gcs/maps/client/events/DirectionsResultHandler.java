package kz.gcs.maps.client.events;

import kz.gcs.maps.client.services.DirectionsResult;
import kz.gcs.maps.client.services.DirectionsStatus;

/**
 * @author Igor Korotkov (igor@ikorotkov.com)
 */
public interface DirectionsResultHandler {
    void handle(long requestId, DirectionsResult result, DirectionsStatus status);
}
