package kz.gcs.maps.client.events;

import kz.gcs.maps.client.LatLon;
import kz.gcs.maps.client.overlays.GoogleMapMarker;

import java.io.Serializable;

/**
 * Interface for listening marker drag events.
 */
public interface MarkerDragListener extends Serializable {
    /**
     * Handle a MarkerDragEvent.
     *
     * @param draggedMarker The marker that was dragged with position updated.
     * @param oldPosition   The old position of the marker.
     */
    void markerDragged(GoogleMapMarker draggedMarker, LatLon oldPosition);
}
