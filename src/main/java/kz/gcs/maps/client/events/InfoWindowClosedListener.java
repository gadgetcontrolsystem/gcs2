package kz.gcs.maps.client.events;

import kz.gcs.maps.client.overlays.GoogleMapInfoWindow;

import java.io.Serializable;

/**
 * Interface for listening info window close events initiated by the user.
 */
public interface InfoWindowClosedListener extends Serializable {

    /**
     * Handle an info window close event
     *
     * @param window the info window that was closed.
     */
    void infoWindowClosed(GoogleMapInfoWindow window);
}
