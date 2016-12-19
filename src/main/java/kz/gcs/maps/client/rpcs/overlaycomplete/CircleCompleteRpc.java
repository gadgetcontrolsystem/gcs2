package kz.gcs.maps.client.rpcs.overlaycomplete;

import com.vaadin.shared.communication.ServerRpc;
import kz.gcs.maps.client.overlays.GoogleMapCircle;

/**
 * @author korotkov
 * @version $Id$
 */
public interface CircleCompleteRpc extends ServerRpc {
    void circleComplete(GoogleMapCircle circle);
}
