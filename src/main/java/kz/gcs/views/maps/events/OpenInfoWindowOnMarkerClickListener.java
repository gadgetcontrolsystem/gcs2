package kz.gcs.views.maps.events;

import kz.gcs.maps.GoogleMap;
import kz.gcs.maps.client.events.click.MarkerClickListener;
import kz.gcs.maps.client.overlays.GoogleMapInfoWindow;
import kz.gcs.maps.client.overlays.GoogleMapMarker;

/**
 * Listener that opens info window when a marker is clicked.
 */
public class OpenInfoWindowOnMarkerClickListener implements MarkerClickListener {

    private static final long serialVersionUID = 646386541641L;

    private final GoogleMap map;
    private final GoogleMapMarker marker;
    private final GoogleMapInfoWindow window;

    public OpenInfoWindowOnMarkerClickListener(GoogleMap map,
                                               GoogleMapMarker marker, GoogleMapInfoWindow window) {
        this.map = map;
        this.marker = marker;
        this.window = window;
    }

    @Override
    public void markerClicked(GoogleMapMarker clickedMarker) {
        if (clickedMarker.equals(marker)) {
            map.openInfoWindow(window);
        }
    }

}
