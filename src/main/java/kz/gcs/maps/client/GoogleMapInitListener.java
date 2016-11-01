package kz.gcs.maps.client;

import com.google.gwt.maps.client.MapWidget;

import java.io.Serializable;

/**
 * A listener which instances are called when the Google Maps API has been
 * loaded and the map object is ready to be used.
 */
public interface GoogleMapInitListener extends Serializable {
    void mapWidgetInitiated(MapWidget map);

    void mapsApiLoaded();
}
