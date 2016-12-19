package kz.gcs.maps.client.events.overlaycomplete;

import kz.gcs.maps.client.overlays.GoogleMapPolygon;

/**
 * @author Igor Korotkov (igor@ikorotkov.com)

 */
public interface PolygonCompleteListener {
    void polygonComplete(GoogleMapPolygon polygon);
}
