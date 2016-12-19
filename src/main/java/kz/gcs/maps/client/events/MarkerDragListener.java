package kz.gcs.maps.client.events;

import kz.gcs.maps.client.base.LatLon;
import kz.gcs.maps.client.overlays.GoogleMapMarker;

import java.io.Serializable;

/**
 * Interface for listening marker drag events.
 * 
 * @author Tapio Aali <tapio@vaadin.com>
 */
public interface MarkerDragListener extends Serializable {
    /**
     * Handle a MarkerDragEvent.
     * 
     * @param draggedMarker
     *            The marker that was dragged with position updated.
     * @param oldPosition
     *            The old position of the marker.
     */
    public void markerDragged(GoogleMapMarker draggedMarker, LatLon oldPosition);
}
