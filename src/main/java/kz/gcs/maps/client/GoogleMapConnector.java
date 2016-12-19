package kz.gcs.maps.client;

import com.google.gwt.ajaxloader.client.AjaxLoader;
import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.LoadApi;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.client.ui.layout.ElementResizeEvent;
import com.vaadin.client.ui.layout.ElementResizeListener;
import com.vaadin.shared.ui.Connect;
import kz.gcs.maps.GoogleMap;
import kz.gcs.maps.client.base.LatLon;
import kz.gcs.maps.client.events.*;
import kz.gcs.maps.client.events.centerchange.CircleCenterChangeListener;
import kz.gcs.maps.client.events.click.CircleClickListener;
import kz.gcs.maps.client.events.click.MapClickListener;
import kz.gcs.maps.client.events.click.MarkerClickListener;
import kz.gcs.maps.client.events.click.PolygonClickListener;
import kz.gcs.maps.client.events.doubleclick.CircleDoubleClickListener;
import kz.gcs.maps.client.events.doubleclick.MarkerDoubleClickListener;
import kz.gcs.maps.client.events.overlaycomplete.CircleCompleteListener;
import kz.gcs.maps.client.events.overlaycomplete.PolygonCompleteListener;
import kz.gcs.maps.client.events.radiuschange.CircleRadiusChangeListener;
import kz.gcs.maps.client.overlays.GoogleMapCircle;
import kz.gcs.maps.client.overlays.GoogleMapInfoWindow;
import kz.gcs.maps.client.overlays.GoogleMapMarker;
import kz.gcs.maps.client.overlays.GoogleMapPolygon;
import kz.gcs.maps.client.rpcs.*;
import kz.gcs.maps.client.rpcs.centerchange.CircleCenterChangeRpc;
import kz.gcs.maps.client.rpcs.click.CircleClickedRpc;
import kz.gcs.maps.client.rpcs.click.MapClickedRpc;
import kz.gcs.maps.client.rpcs.click.MarkerClickedRpc;
import kz.gcs.maps.client.rpcs.click.PolygonClickedRpc;
import kz.gcs.maps.client.rpcs.doubleclick.CircleDoubleClickRpc;
import kz.gcs.maps.client.rpcs.doubleclick.MarkerDoubleClickedRpc;
import kz.gcs.maps.client.rpcs.overlaycomplete.CircleCompleteRpc;
import kz.gcs.maps.client.rpcs.overlaycomplete.PolygonCompleteRpc;
import kz.gcs.maps.client.rpcs.radiuschange.CircleRadiusChangeRpc;
import kz.gcs.maps.client.services.DirectionsResult;
import kz.gcs.maps.client.services.DirectionsStatus;

import java.util.ArrayList;

/**
 * The connector for the Google Maps JavaScript API v3.
 * 
 * @author Tapio Aali <tapio@vaadin.com>
 */
@Connect(GoogleMap.class)
public class GoogleMapConnector extends AbstractComponentConnector implements
        MarkerClickListener, MarkerDoubleClickListener, MapMoveListener, MapClickListener,
        MarkerDragListener, InfoWindowClosedListener, PolygonCompleteListener, PolygonEditListener,
        MapInitListener, DirectionsResultHandler, CircleClickListener, CircleDoubleClickListener,
        CircleCompleteListener, CircleRadiusChangeListener, CircleCenterChangeListener, PolygonClickListener {

    private static final long serialVersionUID = 646346521643L;

    protected static boolean apiLoaded = false;
    protected static boolean mapInitiated = false;

    private boolean deferred = false;
    private MarkerClickedRpc markerClickedRpc = RpcProxy.create(MarkerClickedRpc.class, this);
    private MarkerDoubleClickedRpc markerDoubleClickedRpc = RpcProxy.create(MarkerDoubleClickedRpc.class, this);
    private MapMovedRpc mapMovedRpc = RpcProxy.create(MapMovedRpc.class, this);
    private MapInitRpc mapInitRpc = RpcProxy.create(MapInitRpc.class, this);
    private MapClickedRpc mapClickRpc = RpcProxy.create(MapClickedRpc.class, this);
    private MarkerDraggedRpc markerDraggedRpc = RpcProxy.create(MarkerDraggedRpc.class, this);
    private InfoWindowClosedRpc infoWindowClosedRpc = RpcProxy.create(InfoWindowClosedRpc.class, this);
    private PolygonCompleteRpc polygonCompleteRpc = RpcProxy.create(PolygonCompleteRpc.class, this);
    private PolygonEditRpc polygonEditRpc = RpcProxy.create(PolygonEditRpc.class, this);
    private PolygonClickedRpc polygonClickedRpc = RpcProxy.create(PolygonClickedRpc.class, this);
    private HandleDirectionsResultRpc handleDirectionsResultRpc = RpcProxy.create(HandleDirectionsResultRpc.class, this);
    private CircleClickedRpc circleClickedRpc = RpcProxy.create(CircleClickedRpc.class, this);
    private CircleDoubleClickRpc circleDoubleClickRpc = RpcProxy.create(CircleDoubleClickRpc.class, this);
    private CircleCenterChangeRpc circleCenterChangeRpc = RpcProxy.create(CircleCenterChangeRpc.class, this);
    private CircleRadiusChangeRpc circleRadiusChangeRpc = RpcProxy.create(CircleRadiusChangeRpc.class, this);
    private CircleCompleteRpc circleCompleteRpc = RpcProxy.create(CircleCompleteRpc.class, this);

    public GoogleMapConnector() {
    }

    private void initMap() {
        getWidget().setVisualRefreshEnabled(getState().visualRefreshEnabled);
        getWidget().initMap(getState().center, getState().zoom, getState().mapTypeId, this);
        getWidget().setMarkerClickListener(this);
        getWidget().setMarkerDoubleClickListener(this);
        getWidget().setMapMoveListener(this);
        getWidget().setMapClickListener(this);
        getWidget().setMarkerDragListener(this);
        getWidget().setInfoWindowClosedListener(this);
        getWidget().setPolygonCompleteListener(this);
        getWidget().setPolygonEditListener(this);
        getWidget().setPolygonClickListener(this);
        getWidget().setDirectionsResultHandler(this);
        getWidget().setCircleClickListener(this);
        getWidget().setCircleDoubleClickListener(this);
        getWidget().setCircleCompleteListener(this);
        getWidget().setCircleCenterChangeListener(this);
        getWidget().setCircleRadiusChangeListener(this);

        if (deferred) {
            loadDeferred();
            deferred = false;
        }
        getLayoutManager().addElementResizeListener(getWidget().getElement(),
                new ElementResizeListener() {
                    @Override
                    public void onElementResize(ElementResizeEvent e) {
                        getWidget().triggerResize();
                    }
                });
    }

    @Override
    protected Widget createWidget() {
        return GWT.create(GoogleMapWidget.class);
    }

    @Override
    public GoogleMapWidget getWidget() {
        return (GoogleMapWidget) super.getWidget();
    }

    @Override
    public GoogleMapState getState() {
        return (GoogleMapState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
        GoogleMapWidget widget = getWidget();
        // settings that can be set without API being loaded/map initiated
        if (getState().limitCenterBounds) {
            widget.setCenterBoundLimits(getState().centerNELimit,
                    getState().centerSWLimit);
        } else {
            widget.clearCenterBoundLimits();
        }

        if (getState().limitVisibleAreaBounds) {
            widget.setVisibleAreaBoundLimits(
                    getState().visibleAreaNELimit,
                    getState().visibleAreaSWLimit);
        } else {
            widget.clearVisibleAreaBoundLimits();
        }

        // load API/init map
        if (!apiLoaded) {
            deferred = true;
            loadMapApi();
            apiLoaded = true;
            return;
        } else if (!widget.isMapInitiated()) {
            deferred = true;
            initMap();
            return;
        }

        // settings that require initiated map
        boolean initial = stateChangeEvent.isInitialStateChange();
        // do not set zoom/center again if the change originated from client
        if (!getState().locationFromClient || initial) {
            if (getState().center.getLat() != widget.getLatitude()
                    || getState().center.getLon() != widget.getLongitude()) {
                widget.setCenter(getState().center);
            }
            if (getState().zoom != widget.getZoom()) {
                widget.setZoom(getState().zoom);
            }
        }

        if (stateChangeEvent.hasPropertyChanged("markers") || initial) {
            widget.setMarkers(getState().markers.values());
        }

        if (stateChangeEvent.hasPropertyChanged("polygons") || initial) {
            widget.setPolygonOverlays(getState().polygons);
        }

        if (stateChangeEvent.hasPropertyChanged("polylines") || initial) {
            widget.setPolylineOverlays(getState().polylines);
        }

        if (stateChangeEvent.hasPropertyChanged("circles") || initial) {
            widget.setCircleOverlays(getState().circles);
        }

        if (stateChangeEvent.hasPropertyChanged("kmlLayers") || initial) {
            widget.setKmlLayers(getState().kmlLayers);
        }

        if (stateChangeEvent.hasPropertyChanged("heatMapLayers") || initial) {
            widget.setHeatMapLayers(getState().heatMapLayers);
        }

        if (stateChangeEvent.hasPropertyChanged("imageMapTypes") || initial) {
            widget.setImageMapTypes(getState().imageMapTypes);
        }

        if (stateChangeEvent.hasPropertyChanged("overlayImageMapTypes") || initial) {
            widget.setOverlayImageMapTypes(getState().overlayImageMapTypes);
        }

        if (stateChangeEvent.hasPropertyChanged("mapTypeIds") || initial) {
            widget.setMapTypes(getState().mapTypeIds);
        }

        if (stateChangeEvent.hasPropertyChanged("directionsRequests") || initial) {
            widget.processDirectionRequests(getState().directionsRequests.values());
        }

        if (stateChangeEvent.hasPropertyChanged("mapTypeId") || initial) {
            widget.setMapType(getState().mapTypeId);
        }

        if (stateChangeEvent.hasPropertyChanged("controls") || initial) {
            widget.setControls(getState().controls);
        }

        if (stateChangeEvent.hasPropertyChanged("draggable") || initial) {
            widget.setDraggable(getState().draggable);
        }
        if (stateChangeEvent.hasPropertyChanged("keyboardShortcutsEnabled")
                || initial) {
            widget.setKeyboardShortcutsEnabled(
                    getState().keyboardShortcutsEnabled);
        }
        if (stateChangeEvent.hasPropertyChanged("scrollWheelEnabled")
                || initial) {
            widget.setScrollWheelEnabled(getState().scrollWheelEnabled);
        }
        if (stateChangeEvent.hasPropertyChanged("minZoom") || initial) {
            widget.setMinZoom(getState().minZoom);
        }
        if (stateChangeEvent.hasPropertyChanged("maxZoom") || initial) {
            widget.setMaxZoom(getState().maxZoom);
        }

        if (stateChangeEvent.hasPropertyChanged("infoWindows") || initial) {
            widget.setInfoWindows(getState().infoWindows.values());
        }

        if (stateChangeEvent.hasPropertyChanged("visualRefreshEnabled")
                || initial) {
            widget.setVisualRefreshEnabled(getState().visualRefreshEnabled);
        }

        if (stateChangeEvent.hasPropertyChanged("fitToBoundsNE")
                || stateChangeEvent.hasPropertyChanged("fitToBoundsSW")
                || initial) {
            if (getState().fitToBoundsNE != null
                    && getState().fitToBoundsSW != null) {
                widget.fitToBounds(getState().fitToBoundsNE,
                        getState().fitToBoundsSW);
            }
        }

        if (stateChangeEvent.hasPropertyChanged("drawingOptions") || initial) {
            widget.setDrawingOptions(getState().drawingOptions);
        }

        if (initial) {
            widget.triggerResize();
        }

    }

    private void loadMapApi() {
        StringBuilder otherParams = new StringBuilder();
        if (getState().language != null) {
            otherParams.append("&language=").append(getState().language);
        }

        ArrayList<LoadApi.LoadLibrary> loadLibraries = new ArrayList<LoadApi.LoadLibrary>();
        loadLibraries.add(LoadApi.LoadLibrary.DRAWING);
        loadLibraries.add(LoadApi.LoadLibrary.VISUALIZATION);

        Runnable callback = new Runnable() {
            public void run() {
                initMap();
            }
        };

        AjaxLoader.init(getState().apiKey);
        
        LoadApi.go(callback, loadLibraries, false, otherParams.toString());
    }

    private void loadDeferred() {
        getWidget().setMarkers(getState().markers.values());
        getWidget().setPolygonOverlays(getState().polygons);
        getWidget().setPolylineOverlays(getState().polylines);
        getWidget().setCircleOverlays(getState().circles);
        getWidget().setKmlLayers(getState().kmlLayers);
        getWidget().setHeatMapLayers(getState().heatMapLayers);
        getWidget().setImageMapTypes(getState().imageMapTypes);
        getWidget().setOverlayImageMapTypes(getState().overlayImageMapTypes);
        getWidget().setInfoWindows(getState().infoWindows.values());
        getWidget().setMapTypes(getState().mapTypeIds);
        getWidget().setMapType(getState().mapTypeId);
        getWidget().setControls(getState().controls);
        getWidget().setDraggable(getState().draggable);
        getWidget().setKeyboardShortcutsEnabled(
                getState().keyboardShortcutsEnabled);
        getWidget().setScrollWheelEnabled(getState().scrollWheelEnabled);
        getWidget().setMinZoom(getState().minZoom);
        getWidget().setMaxZoom(getState().maxZoom);
        getWidget().setDrawingOptions(getState().drawingOptions);
        getWidget().processDirectionRequests(getState().directionsRequests.values());
        if (getState().fitToBoundsNE != null
                && getState().fitToBoundsSW != null) {
            getWidget().fitToBounds(getState().fitToBoundsNE,
                    getState().fitToBoundsSW);
        }
    }

    @Override
    public void markerClicked(GoogleMapMarker clickedMarker) {
        markerClickedRpc.markerClicked(clickedMarker.getId());
    }

    @Override
    public void markerDoubleClicked(GoogleMapMarker clickedMarker) {
        markerDoubleClickedRpc.markerClicked(clickedMarker.getId());
    }

    @Override
    public void mapMoved(int zoomLevel, LatLon center, LatLon boundsNE,
            LatLon boundsSW) {
        mapMovedRpc.mapMoved(zoomLevel, center, boundsNE, boundsSW);
    }

    @Override
    public void init(LatLon center, int zoom, LatLon boundsNE, LatLon boundsSW) {
        mapInitRpc.init(center, zoom, boundsNE, boundsSW);
    }

    @Override
    public void markerDragged(GoogleMapMarker draggedMarker, LatLon oldPosition) {
        markerDraggedRpc.markerDragged(draggedMarker.getId(),
                draggedMarker.getPosition());
    }

    @Override
    public void infoWindowClosed(GoogleMapInfoWindow window) {
        infoWindowClosedRpc.infoWindowClosed(window.getId());
    }

    @Override
    public void polygonComplete(GoogleMapPolygon polygon) {
        polygonCompleteRpc.polygonComplete(polygon);
    }

    @Override
    public void mapClicked(LatLon position) {
        mapClickRpc.mapClicked(position);
    }

    @Override
    public void polygonEdited(GoogleMapPolygon polygon, ActionType actionType, int idx, LatLon latLon) {
        polygonEditRpc.polygonEdited(polygon.getId(), actionType, idx, latLon);
    }

    @Override
    public void polygonClicked(GoogleMapPolygon polygon) {
        polygonClickedRpc.polygonClicked(polygon.getId());
    }

    @Override
    public void handle(long requestId, DirectionsResult result, DirectionsStatus status) {
        handleDirectionsResultRpc.handle(result, status, requestId);
    }

    @Override
    public void radiusChange(GoogleMapCircle circle, double oldRadius) {
        circleRadiusChangeRpc.radiusChanged(circle.getId(), circle.getRadius());
    }

    @Override
    public void circleDoubleClicked(GoogleMapCircle circle) {
        circleDoubleClickRpc.circleDoubleClicked(circle.getId());
    }

    @Override
    public void circleComplete(GoogleMapCircle circle) {
        circleCompleteRpc.circleComplete(circle);
    }

    @Override
    public void circleClicked(GoogleMapCircle clickedCircle) {
        circleClickedRpc.circleClicked(clickedCircle.getId());
    }

    @Override
    public void centerChanged(GoogleMapCircle circle, LatLon oldCenter) {
        circleCenterChangeRpc.centerChanged(circle.getId(), circle.getCenter());
    }
}
