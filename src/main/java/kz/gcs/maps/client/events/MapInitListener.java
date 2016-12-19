package kz.gcs.maps.client.events;

import kz.gcs.maps.client.base.LatLon;

/**
 * @author Igor Korotkov (igor@ikorotkov.com)
 */
public interface MapInitListener {
    public void init(LatLon center, int zoom, LatLon boundsNE, LatLon boundsSW);
}
