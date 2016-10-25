package kz.gcs.maps.client;

import com.vaadin.shared.AbstractComponentState;
import kz.gcs.maps.client.layers.GoogleMapKmlLayer;
import kz.gcs.maps.client.overlays.GoogleMapInfoWindow;
import kz.gcs.maps.client.overlays.GoogleMapMarker;
import kz.gcs.maps.client.overlays.GoogleMapPolygon;
import kz.gcs.maps.client.overlays.GoogleMapPolyline;

import java.util.*;

/**
 * The shared state of the Google Maps. Contains also the default values.
 */
public class GoogleMapState extends AbstractComponentState {
    private static final long serialVersionUID = 646346522643L;

    public String apiKey = null;
    public String clientId = null;

    // defaults to the language setting of the browser
    public String language = null;
    public String mapTypeId = "Roadmap";
    public LatLon center = new LatLon(51.477811, -0.001475);
    public int zoom = 8;
    public int maxZoom = 21;
    public int minZoom = 0;

    public boolean draggable = true;
    public boolean keyboardShortcutsEnabled = true;
    public boolean scrollWheelEnabled = true;

    public Set<kz.gcs.maps.client.GoogleMapControl> controls = new HashSet<kz.gcs.maps.client.GoogleMapControl>(
        Arrays.asList(kz.gcs.maps.client.GoogleMapControl.MapType, kz.gcs.maps.client.GoogleMapControl.Pan,
            kz.gcs.maps.client.GoogleMapControl.Rotate, kz.gcs.maps.client.GoogleMapControl.Scale,
            kz.gcs.maps.client.GoogleMapControl.StreetView, GoogleMapControl.Zoom));

    public boolean limitCenterBounds = false;
    public LatLon centerSWLimit = new LatLon(0.0, 0.0);
    public LatLon centerNELimit = new LatLon(0.0, 0.0);

    public boolean limitVisibleAreaBounds = false;
    public LatLon visibleAreaSWLimit = new LatLon(0.0, 0.0);
    public LatLon visibleAreaNELimit = new LatLon(0.0, 0.0);

    public LatLon fitToBoundsNE = null;
    public LatLon fitToBoundsSW = null;

    public Set<GoogleMapPolygon> polygons = new HashSet<GoogleMapPolygon>();
    public Set<GoogleMapPolyline> polylines = new HashSet<GoogleMapPolyline>();
    public Set<GoogleMapKmlLayer> kmlLayers = new HashSet<GoogleMapKmlLayer>();

    public Map<Long, GoogleMapMarker> markers = new HashMap<Long, GoogleMapMarker>();

    public Map<Long, GoogleMapInfoWindow> infoWindows = new HashMap<Long, GoogleMapInfoWindow>();
    public boolean trafficLayerVisible = false;

    public String apiUrl = null;

    public Map<Long, String> infoWindowContentIdentifiers = new HashMap<>();
}