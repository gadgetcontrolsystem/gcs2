package kz.gcs.maps.client.events.click;

import kz.gcs.maps.client.overlays.GoogleMapPolygon;

import java.io.Serializable;

/**
 * @author korotkov
 * @version $Id$
 */
public interface PolygonClickListener extends Serializable {
    public void polygonClicked(GoogleMapPolygon polygon);
}
