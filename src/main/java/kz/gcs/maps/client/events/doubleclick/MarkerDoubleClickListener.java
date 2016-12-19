package kz.gcs.maps.client.events.doubleclick;

import kz.gcs.maps.client.overlays.GoogleMapMarker;

/**
 * @author Igor Korotkov (igor@ikorotkov.com)

 */
public interface MarkerDoubleClickListener {
    /**
     * Handle a MarkerDoubleClickEvent.
     *
     * @param clickedMarker
     *            The marker that was clicked.
     */
    public void markerDoubleClicked(GoogleMapMarker clickedMarker);
}
