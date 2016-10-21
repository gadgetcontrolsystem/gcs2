package kz.gcs.maps.client.events;

import kz.gcs.maps.client.LatLon;

import java.io.Serializable;

/**
 * Interface for listening map click events.
 */
public interface MapClickListener extends Serializable {
    /**
     * Handle a MapClickListener.
     *
     * @param position The position that was clicked.
     */
    void mapClicked(LatLon position);
}
