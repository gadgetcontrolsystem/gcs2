package kz.gcs.maps.client.events.click;

import kz.gcs.maps.client.overlays.GoogleMapCircle;

/**
 * @author korotkov
 * @version $Id$
 */
public interface CircleClickListener {
    void circleClicked(GoogleMapCircle clickedCircle);
}
