package kz.gcs.maps.client.rpcs.click;

import com.vaadin.shared.communication.ServerRpc;

/**
 * @author korotkov
 * @version $Id$
 */
public interface CircleClickedRpc extends ServerRpc {
    void circleClicked(long circleId);
}
