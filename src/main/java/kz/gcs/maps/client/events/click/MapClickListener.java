package kz.gcs.maps.client.events.click;

import kz.gcs.maps.client.base.LatLon;

import java.io.Serializable;

/**
 * Interface for listening map click events.
 * 
 */
public interface MapClickListener extends Serializable {
    /**
     * Handle a MapClickListener.
     * 
     * @param position
     *            The position that was clicked.
     */
    public void mapClicked(LatLon position);
}
