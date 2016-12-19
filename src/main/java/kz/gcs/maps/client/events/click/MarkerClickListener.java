package kz.gcs.maps.client.events.click;

import kz.gcs.maps.client.overlays.GoogleMapMarker;

import java.io.Serializable;

/**
 * Interface for listening marker click events.
 * 
 */
public interface MarkerClickListener extends Serializable {
    /**
     * Handle a MarkerClickEvent.
     * 
     * @param clickedMarker
     *            The marker that was clicked.
     */
    public void markerClicked(GoogleMapMarker clickedMarker);
}
