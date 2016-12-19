package kz.gcs.maps.client.rpcs.click;

import com.vaadin.shared.communication.ServerRpc;

/**
 * @author korotkov
 * @version $Id$
 */
public interface PolygonClickedRpc extends ServerRpc {
    public void polygonClicked(long polygonId);
}
