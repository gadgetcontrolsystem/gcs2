package kz.gcs.views.maps;

import com.google.common.eventbus.Subscribe;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import kz.gcs.MyUI;
import kz.gcs.domain.Location;
import kz.gcs.event.DashboardEvent;
import kz.gcs.event.DashboardEventBus;
import kz.gcs.maps.GoogleMap;
import kz.gcs.maps.client.GoogleMapControl;
import kz.gcs.maps.client.LatLon;
import kz.gcs.maps.client.events.MarkerClickListener;
import kz.gcs.maps.client.layers.GoogleMapKmlLayer;
import kz.gcs.maps.client.overlays.GoogleMapInfoWindow;
import kz.gcs.maps.client.overlays.GoogleMapMarker;
import kz.gcs.maps.client.overlays.GoogleMapPolygon;
import kz.gcs.maps.client.overlays.GoogleMapPolyline;
import kz.gcs.views.maps.events.OpenInfoWindowOnMarkerClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MapView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "map";
    public static final String VIEW_TITLE = "карта";
    private static final long serialVersionUID = -379716808231511716L;

    private GoogleMap googleMap;

    private Button componentToMaariaInfoWindowButton;
    private final String apiKey = "AIzaSyDmJQfLtxIo8u4dzu6FYsmHSFRSebEqp6k";

    public MapView() {
        setSizeFull();
        CssLayout rootLayout = new CssLayout();
        rootLayout.setSizeFull();
        addComponent(rootLayout);
        setExpandRatio(rootLayout, 1);

        TabSheet tabs = new TabSheet();
        tabs.setSizeFull();
        rootLayout.addComponent(tabs);

        VerticalLayout mapContent = new VerticalLayout();
        mapContent.setSizeFull();
        tabs.addTab(mapContent, "Карты Google");
        tabs.addTab(new Label("Страница находится в разработке"), "Карты Yandex");

        googleMap = new GoogleMap(this.apiKey, null, "Russian");
        googleMap.setDraggable(true);

        googleMap.setZoom(10);
        googleMap.setSizeFull();

        Location lastLocation = MyUI.getDataProvider().getLastLocation(0);
        LatLon position = new LatLon(
                lastLocation.getLat(), lastLocation.getLon());
        googleMap.addMarker(lastLocation.displayStr(), position, false, null);

        googleMap.setCenter(position);
        googleMap.setMinZoom(4);
        googleMap.setMaxZoom(16);


        mapContent.addComponent(googleMap);
        mapContent.setExpandRatio(googleMap, 1.0f);

        HorizontalLayout buttonLayoutRow1 = new HorizontalLayout();
        buttonLayoutRow1.setHeight("26px");
        mapContent.addComponent(buttonLayoutRow1);

        HorizontalLayout buttonLayoutRow2 = new HorizontalLayout();
        buttonLayoutRow2.setHeight("26px");
        mapContent.addComponent(buttonLayoutRow2);

        drawButtons(buttonLayoutRow1, buttonLayoutRow2);


        DashboardEventBus.register(this);
    }

    @Subscribe
    public void createTransactionReport(final DashboardEvent.TransactionReportEvent event) {
        googleMap.clearMarkers();
        googleMap.clearMarkerClickListeners();
        List<Location> locations = new ArrayList<>(event.getLocations());
        if(locations.size()==0)
            return;
        Collections.sort(locations);
        placeMarkersOnMap(locations);
        DashboardEventBus.post(new DashboardEvent.ReportsCountUpdatedEvent(
                getComponentCount() - 1));
    }

    private void placeMarkersOnMap(List<Location> locations) {
        for (Location temp : locations) {
            GoogleMapMarker marker = new GoogleMapMarker(temp.getCity(), new LatLon(temp.getLat(), temp.getLon()), false);
            googleMap.addMarker(marker);
            GoogleMapInfoWindow window = new GoogleMapInfoWindow(temp.getTime() + ", " + temp.getCity() + ", " + temp.getCountry(), marker);
            OpenInfoWindowOnMarkerClickListener windowOpener = new OpenInfoWindowOnMarkerClickListener(googleMap, marker, window);
            googleMap.addMarkerClickListener(windowOpener);
        }
        Location latest = locations.get(locations.size()-1);
        googleMap.setCenter(new LatLon(latest.getLat(), latest.getLon()));
    }

    private void drawButtons(HorizontalLayout buttonLayoutRow1, HorizontalLayout buttonLayoutRow2) {
        Button moveCenterButton = new Button(
                "Move over Astana (51.1605, 71.4704), zoom 12",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        googleMap.setCenter(new LatLon(51.1605, 71.4704));
                        googleMap.setZoom(12);
                    }
                });
        buttonLayoutRow1.addComponent(moveCenterButton);

        Button limitCenterButton = new Button(
                "Limit center between (60.619324, 22.712753), (60.373484, 21.945083)",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        googleMap.setCenterBoundLimits(new LatLon(60.619324,
                                22.712753), new LatLon(60.373484, 21.945083));
                        event.getButton().setEnabled(false);
                    }
                });
        buttonLayoutRow1.addComponent(limitCenterButton);

        Button limitVisibleAreaButton = new Button(
                "Limit visible area between (60.494439, 22.397835), (60.373484, 21.945083)",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        googleMap.setVisibleAreaBoundLimits(new LatLon(
                                60.494439, 22.397835), new LatLon(60.420632,
                                22.138626));
                        event.getButton().setEnabled(false);
                    }
                });
        buttonLayoutRow1.addComponent(limitVisibleAreaButton);

        Button zoomToBoundsButton = new Button("Zoom to bounds",
                new Button.ClickListener() {

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        googleMap.fitToBounds(new LatLon(60.45685853323144,
                                22.320034754486073), new LatLon(
                                60.4482979242303, 22.27887893936156));

                    }
                });
        buttonLayoutRow1.addComponent(zoomToBoundsButton);



        Button addPolyOverlayButton = new Button("Add overlay over Luonnonmaa",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        ArrayList<LatLon> points = new ArrayList<LatLon>();
                        points.add(new LatLon(60.484715, 21.923706));
                        points.add(new LatLon(60.446636, 21.941387));
                        points.add(new LatLon(60.422496, 21.99546));
                        points.add(new LatLon(60.427326, 22.06464));
                        points.add(new LatLon(60.446467, 22.064297));

                        GoogleMapPolygon overlay = new GoogleMapPolygon(points,
                                "#ae1f1f", 0.8, "#194915", 0.5, 3);
                        googleMap.addPolygonOverlay(overlay);
                        event.getButton().setEnabled(false);
                    }
                });
        buttonLayoutRow2.addComponent(addPolyOverlayButton);

        Button addPolyLineButton = new Button("Draw line from Turku to Raisio",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        ArrayList<LatLon> points = new ArrayList<LatLon>();
                        points.add(new LatLon(60.448118, 22.253738));
                        points.add(new LatLon(60.455144, 22.24198));
                        points.add(new LatLon(60.460222, 22.211939));
                        points.add(new LatLon(60.488224, 22.174602));
                        points.add(new LatLon(60.486025, 22.169195));

                        GoogleMapPolyline overlay = new GoogleMapPolyline(
                                points, "#d31717", 0.8, 10);
                        googleMap.addPolyline(overlay);
                        event.getButton().setEnabled(false);
                    }
                });
        buttonLayoutRow2.addComponent(addPolyLineButton);
        Button addPolyLineButton2 = new Button(
                "Draw line from Turku to Raisio2", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                ArrayList<LatLon> points2 = new ArrayList<LatLon>();
                points2.add(new LatLon(60.448118, 22.253738));
                points2.add(new LatLon(60.486025, 22.169195));
                GoogleMapPolyline overlay2 = new GoogleMapPolyline(
                        points2, "#d31717", 0.8, 10);
                googleMap.addPolyline(overlay2);
                event.getButton().setEnabled(false);
            }
        });
        buttonLayoutRow2.addComponent(addPolyLineButton2);
        Button changeToTerrainButton = new Button("Change to terrain map",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        googleMap.setMapType(GoogleMap.MapType.Terrain);
                        event.getButton().setEnabled(false);
                    }
                });
        buttonLayoutRow2.addComponent(changeToTerrainButton);

        Button changeControls = new Button("Remove street view control",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        googleMap.removeControl(GoogleMapControl.StreetView);
                        event.getButton().setEnabled(false);
                    }
                });
        buttonLayoutRow2.addComponent(changeControls);


        Button addKmlLayerButton = new Button("Add KML layer",
                new Button.ClickListener() {

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        googleMap
                                .addKmlLayer(new GoogleMapKmlLayer(
                                        "http://maps.google.it/maps/"
                                                + "ms?authuser=0&ie=UTF8&hl=it&oe=UTF8&msa=0&"
                                                + "output=kml&msid=212897908682884215672.0004ecbac547d2d635ff5"));
                    }
                });
        buttonLayoutRow2.addComponent(addKmlLayerButton);

        Button clearMarkersButton = new Button("Remove all markers",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        googleMap.clearMarkers();
                    }
                });

        Button trafficLayerButton = new Button("Toggle Traffic Layer",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        googleMap.setTrafficLayerVisible(
                                !googleMap.isTrafficLayerVisible());
                    }
                });
        buttonLayoutRow2.addComponent(trafficLayerButton);
    }

    private void drawConsolePanel() {
        /*Panel console = new Panel();
        console.setHeight("100px");
        final CssLayout consoleLayout = new CssLayout();
        console.setContent(consoleLayout);
        mapContent.addComponent(console);*/

        /*googleMap.addMarkerClickListener(new MarkerClickListener() {
            @Override
            public void markerClicked(GoogleMapMarker clickedMarker) {
                Label consoleEntry = new Label("Marker \""
                        + clickedMarker.getCaption() + "\" at ("
                        + clickedMarker.getPosition().getLat() + ", "
                        + clickedMarker.getPosition().getLon() + ") clicked.");
                consoleLayout.addComponent(consoleEntry, 0);
            }
        });

        googleMap.addMapMoveListener(new MapMoveListener() {
            @Override
            public void mapMoved(int zoomLevel, LatLon center, LatLon boundsNE,
                                 LatLon boundsSW) {
                Label consoleEntry = new Label("Map moved to ("
                        + center.getLat() + ", " + center.getLon() + "), zoom "
                        + zoomLevel + ", boundsNE: (" + boundsNE.getLat()
                        + ", " + boundsNE.getLon() + "), boundsSW: ("
                        + boundsSW.getLat() + ", " + boundsSW.getLon() + ")");
                consoleLayout.addComponent(consoleEntry, 0);
            }
        });

        googleMap.addMapClickListener(new MapClickListener() {
            @Override
            public void mapClicked(LatLon position) {
                Label consoleEntry = new Label("Map click to ("
                        + position.getLat() + ", " + position.getLon() + ")");
                consoleLayout.addComponent(consoleEntry, 0);
            }
        });

        googleMap.addMarkerDragListener(new MarkerDragListener() {
            @Override
            public void markerDragged(GoogleMapMarker draggedMarker,
                                      LatLon oldPosition) {
                Label consoleEntry = new Label("Marker \""
                        + draggedMarker.getCaption() + "\" dragged from ("
                        + oldPosition.getLat() + ", " + oldPosition.getLon()
                        + ") to (" + draggedMarker.getPosition().getLat()
                        + ", " + draggedMarker.getPosition().getLon() + ")");
                consoleLayout.addComponent(consoleEntry, 0);
            }
        });

        googleMap.addInfoWindowClosedListener(new InfoWindowClosedListener() {

            @Override
            public void infoWindowClosed(GoogleMapInfoWindow window) {
                Label consoleEntry = new Label("InfoWindow \""
                        + window.getContent() + "\" closed");
                consoleLayout.addComponent(consoleEntry, 0);
            }
        });*/

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

}
