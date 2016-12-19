package kz.gcs.maps.client.services;

/**
 * @author Igor Korotkov (igor@ikorotkov.com)
 */
public interface DirectionsResultCallback {
    void onCallback(DirectionsResult result, DirectionsStatus status);
}
