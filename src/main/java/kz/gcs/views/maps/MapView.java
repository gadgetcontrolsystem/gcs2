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
import kz.gcs.maps.client.LatLon;
import kz.gcs.maps.client.overlays.GoogleMapInfoWindow;
import kz.gcs.maps.client.overlays.GoogleMapMarker;
import kz.gcs.maps.client.overlays.GoogleMapPolyline;
import kz.gcs.views.maps.events.OpenInfoWindowOnMarkerClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class MapView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "map";
    public static final String VIEW_TITLE = "карта";
    private static final long serialVersionUID = -379716808231511716L;

    private GoogleMap googleMap;
    private Label currentTimeStamp;

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

        googleMap.setZoom(15);
        googleMap.setSizeFull();

        getLastLocation();
        googleMap.setMinZoom(4);
        googleMap.setMaxZoom(20);


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
        googleMap.clearAll();
        List<Location> locations = new ArrayList<>(event.getLocations());
        if (locations.size() == 0)
            return;
        Collections.sort(locations);
        placeMarkersOnMap(locations);
        googleMap.setZoom(15);
        DashboardEventBus.post(new DashboardEvent.ReportsCountUpdatedEvent(
                getComponentCount() - 1));
    }

    private void placeMarkersOnMap(List<Location> locations) {
        List<LatLon> latLons = new ArrayList<>();
        String url = "http://mt.google.com/vt/icon/name=icons/spotlight/measle_green_8px.png&scale=2";
        int counter = 1;
        /*LatLon maxLatLon = new LatLon(0, 0);
        LatLon minLatLon = new LatLon(0, 0);*/
        for (Location temp : locations) {
            LatLon latLon = new LatLon(temp.getLat(), temp.getLon());
            latLons.add(latLon);

            /*if(maxLatLon.getLat()<temp.getLat()){
                maxLatLon.setLat(temp.getLat());
            }
            if(maxLatLon.getLon()<temp.getLon()){
                maxLatLon.setLon(temp.getLon());
            }
            if(minLatLon.getLat()>temp.getLat()){
                minLatLon.setLat(temp.getLat());
            }
            if(minLatLon.getLon()>temp.getLon()){
                minLatLon.setLon(temp.getLon());
            }
*/
            if(counter==1){
                url = "http://mt.google.com/vt/icon?color=ff004C13&name=icons/spotlight/spotlight-waypoint-a.png";
            } else if (counter==locations.size()){
                url = "http://mt.google.com/vt/icon?color=ff004C13&name=icons/spotlight/spotlight-waypoint-b.png";
            }

            GoogleMapMarker marker = new GoogleMapMarker(temp.displayStr(), new LatLon(temp.getLat(), temp.getLon()), false, url);
            googleMap.addMarker(marker);
            GoogleMapInfoWindow window = new GoogleMapInfoWindow(temp.getTime() + ", " + temp.getCity() + ", " + temp.getCountry()+" Lat: "+temp.getLat()+" Lon: "+temp.getLon(), marker);
            OpenInfoWindowOnMarkerClickListener windowOpener = new OpenInfoWindowOnMarkerClickListener(googleMap, marker, window);
            googleMap.addMarkerClickListener(windowOpener);

            counter++;
        }
        /*googleMap.setCenterBoundLimits(maxLatLon, minLatLon);
        googleMap.setCenterBoundLimitsEnabled(true);*/
        GoogleMapPolyline mapPolyline = new GoogleMapPolyline(latLons, "#2e96db", 0.8, 2);
        googleMap.addPolyline(mapPolyline);
        Location latest = locations.get(locations.size() - 1);
        googleMap.setCenter(new LatLon(latest.getLat(), latest.getLon()));
    }

    private void getLastLocation() {
        Location lastLocation = MyUI.getDataProvider().getLastLocation(0);
        if (lastLocation != null) {
            LatLon position = new LatLon(lastLocation.getLat(), lastLocation.getLon());
            googleMap.addMarker(lastLocation.displayStr(), position, false, null);
            googleMap.setCenter(position);
        }
        googleMap.setZoom(15);
    }

    private void drawButtons(HorizontalLayout buttonLayoutRow1, HorizontalLayout buttonLayoutRow2) {

        Button refreshButton = new Button("Обновить карту", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                googleMap.clearAll();
                getLastLocation();
                currentTimeStamp.setValue("Последнее обновление карты: " + new Date().toString());
            }
        });
        buttonLayoutRow1.addComponent(refreshButton);

        currentTimeStamp = new Label("Последнее обновление карты: " + new Date().toString());

        buttonLayoutRow1.addComponent(currentTimeStamp);

        buttonLayoutRow1.setSpacing(true);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

}
