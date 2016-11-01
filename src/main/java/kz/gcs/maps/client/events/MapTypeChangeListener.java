package kz.gcs.maps.client.events;

import com.google.gwt.maps.client.MapTypeId;

import java.io.Serializable;

public interface MapTypeChangeListener extends Serializable {
    /**
     * Handle a MapTypeIdChange.
     *
     * @param mapTypeId The id of the new map type.
     */
    void mapTypeChanged(MapTypeId mapTypeId);
}
