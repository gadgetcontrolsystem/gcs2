package kz.gcs.maps.client.events.centerchange;

import kz.gcs.maps.client.base.LatLon;
import kz.gcs.maps.client.overlays.GoogleMapCircle;

/**
 * @author korotkov
 * @version $Id$
 */
public interface CircleCenterChangeListener {
    void centerChanged(GoogleMapCircle circle, LatLon oldCenter);
}
