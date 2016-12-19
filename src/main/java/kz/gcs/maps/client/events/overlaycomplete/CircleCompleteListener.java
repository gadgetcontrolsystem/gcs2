package kz.gcs.maps.client.events.overlaycomplete;

import kz.gcs.maps.client.overlays.GoogleMapCircle;

/**
 * @author korotkov
 * @version $Id$
 */
public interface CircleCompleteListener {
    void circleComplete(GoogleMapCircle circle);
}
