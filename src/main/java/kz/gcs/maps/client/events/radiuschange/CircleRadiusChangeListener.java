package kz.gcs.maps.client.events.radiuschange;

import kz.gcs.maps.client.overlays.GoogleMapCircle;

/**
 * @author korotkov
 * @version $Id$
 */
public interface CircleRadiusChangeListener {
    void radiusChange(GoogleMapCircle circle, double oldRadius);
}
