package kz.gcs.maps.client.events;

import kz.gcs.maps.client.base.LatLon;
import kz.gcs.maps.client.overlays.GoogleMapPolygon;

/**
 * @author Igor Korotkov (igor@ikorotkov.com)

 */
public interface PolygonEditListener {

    /**
     * Polygon edit action type
     *
     * <p/> {@link #INSERT} - vertex have been inserted into polygon
     * <p/> {@link #REMOVE} - vertex have been removed from polygon
     * <p/> {@link #SET} - vertex coordinates have been changed
     */
    enum ActionType {
        INSERT,
        REMOVE,
        SET
    }

    void polygonEdited(GoogleMapPolygon polygon, ActionType actionType, int idx, LatLon latLon);
}
