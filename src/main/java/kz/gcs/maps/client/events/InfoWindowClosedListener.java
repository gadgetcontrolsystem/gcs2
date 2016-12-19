package kz.gcs.maps.client.events;

import java.io.Serializable;

import kz.gcs.maps.client.overlays.GoogleMapInfoWindow;

/**
 * Interface for listening info window close events initiated by the user.
 * 
 * @author Tapio Aali <tapio@vaadin.com>
 */
public interface InfoWindowClosedListener extends Serializable {

    /**
     * Handle an info window close event
     * 
     * @param window
     *            the info window that was closed.
     */
    public void infoWindowClosed(GoogleMapInfoWindow window);
}
