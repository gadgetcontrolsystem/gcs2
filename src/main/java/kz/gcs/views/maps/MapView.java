package kz.gcs.views.maps;

import com.google.common.eventbus.Subscribe;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import kz.gcs.MyUI;
import kz.gcs.domain.Location;
import kz.gcs.event.DashboardEvent;
import kz.gcs.event.DashboardEvent.TransactionReportEvent;
import kz.gcs.event.DashboardEventBus;
import kz.gcs.maps.GoogleMap;
import kz.gcs.maps.client.base.LatLon;
import kz.gcs.maps.client.overlays.GoogleMapCircle;
import kz.gcs.maps.client.overlays.GoogleMapInfoWindow;
import kz.gcs.maps.client.overlays.GoogleMapMarker;
import kz.gcs.maps.client.overlays.GoogleMapPolyline;
import kz.gcs.util.AllUtils;
import kz.gcs.views.maps.events.OpenInfoWindowOnMarkerClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class MapView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "map";
    public static final String VIEW_TITLE = "карта";
    private static final long serialVersionUID = -379716808231511716L;
    private Window datesWindow;

    private GoogleMap googleMap;

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

        Location location = MyUI.getDataProvider().getLastLocation(0);
        googleMap = new GoogleMap(new LatLon(location.getLat(), location.getLon()), 15, this.apiKey);
        googleMap.setDraggable(true);

        googleMap.setZoom(15);
        googleMap.setSizeFull();

        getLastLocation();
        googleMap.setMinZoom(4);
        googleMap.setMaxZoom(20);


        mapContent.addComponent(googleMap);
        mapContent.setExpandRatio(googleMap, 1.0f);

        HorizontalLayout buttonLayoutRow = new HorizontalLayout();
        mapContent.addComponent(buttonLayoutRow);
        mapContent.setComponentAlignment(buttonLayoutRow, Alignment.MIDDLE_CENTER);

        drawButtons(buttonLayoutRow);


        DashboardEventBus.register(this);
    }

    @Subscribe
    public void createTransactionReport(final TransactionReportEvent event) {
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
        LatLon maxLatLon = new LatLon(0, 0);
        LatLon minLatLon = new LatLon(90, 90);
        for (Location temp : locations) {
            LatLon latLon = new LatLon(temp.getLat(), temp.getLon());
            latLons.add(latLon);

            if (maxLatLon.getLat() < temp.getLat()) {
                maxLatLon.setLat(temp.getLat());
            }
            if (maxLatLon.getLon() < temp.getLon()) {
                maxLatLon.setLon(temp.getLon());
            }
            if (minLatLon.getLat() > temp.getLat()) {
                minLatLon.setLat(temp.getLat());
            }
            if (minLatLon.getLon() > temp.getLon()) {
                minLatLon.setLon(temp.getLon());
            }

            if (counter == 1) {
                url = "http://mt.google.com/vt/icon?color=ff004C13&name=icons/spotlight/spotlight-waypoint-a.png";
            } else if (counter == locations.size()) {
                url = "http://mt.google.com/vt/icon?color=ff004C13&name=icons/spotlight/spotlight-waypoint-b.png";
            }

            GoogleMapMarker marker = new GoogleMapMarker(temp.displayStr(), new LatLon(temp.getLat(), temp.getLon()), false, url);
            googleMap.addMarker(marker);
            GoogleMapInfoWindow window = new GoogleMapInfoWindow(AllUtils.dateToStrDateTimeP(temp.getTime(), "Время не доступно") + ", " + temp.getCity() + ", " + temp.getCountry() + " Lat: " + temp.getLat() + " Lon: " + temp.getLon(), marker);
            OpenInfoWindowOnMarkerClickListener windowOpener = new OpenInfoWindowOnMarkerClickListener(googleMap, marker, window);
            googleMap.addMarkerClickListener(windowOpener);

            counter++;
        }
        googleMap.fitToBounds(maxLatLon, minLatLon);

        GoogleMapPolyline mapPolyline = new GoogleMapPolyline(latLons, "#2e96db", 0.8, 2);
        googleMap.addPolyline(mapPolyline);
        Location latest = locations.get(locations.size() - 1);
        googleMap.setCenter(new LatLon(latest.getLat(), latest.getLon()));
    }

    private void getLastLocation() {


        Location lastLocation = MyUI.getDataProvider().getLastLocation();
        if (lastLocation != null) {
            googleMap.clearAll();
            LatLon position = new LatLon(lastLocation.getLat(), lastLocation.getLon());
            GoogleMapMarker marker = new GoogleMapMarker(lastLocation.displayStr(), position, false, "http://mt.google.com/vt/icon?color=ff004C13&name=icons/spotlight/spotlight-waypoint-b.png");
            googleMap.addMarker(marker);
            googleMap.setCenter(position);
            GoogleMapCircle mapCircle = new GoogleMapCircle(position, lastLocation.getAccuracy());
            googleMap.addCircleOverlay(mapCircle);
            GoogleMapInfoWindow window = new GoogleMapInfoWindow(AllUtils.dateToStrDateTimeP(lastLocation.getTime(), "Время не доступно") + ", " + lastLocation.getCity() + ", " + lastLocation.getCountry()+" Lat: "+lastLocation.getLat()+" Lon: "+lastLocation.getLon(), marker);
            OpenInfoWindowOnMarkerClickListener windowOpener = new OpenInfoWindowOnMarkerClickListener(googleMap, marker, window);
            googleMap.addMarkerClickListener(windowOpener);
        }
        googleMap.setZoom(15);
    }

    @SuppressWarnings("all")
    private void drawButtons(HorizontalLayout buttonLayoutRow) {

        Button refreshButton = new Button(null, new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                googleMap.clearAll();
                getLastLocation();
            }
        });
        refreshButton.setIcon(FontAwesome.REFRESH, "Обновить карту");
        buttonLayoutRow.addComponent(refreshButton);

        final DateField beforeDateField = new DateField("С", new Date());
        beforeDateField.setResolution(Resolution.MINUTE);
        beforeDateField.setDateFormat("dd.MM.yyyy HH:mm:ss");
        final PopupDateField afterDateField = new PopupDateField("По", new Date());
        afterDateField.setResolution(Resolution.MINUTE);
        afterDateField.setDateFormat("dd.MM.yyyy HH:mm:ss");

        Button submitButton = new Button("Найти", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                googleMap.clearAll();
                Date beforeDate = beforeDateField.getValue();
                Date afterDate = afterDateField.getValue();
                List<Location> locations = new ArrayList(MyUI.getDataProvider().getLocationsBetween(beforeDate, afterDate));
                if (locations.size() == 0) {
                    Notification.show("По этому отрезку времени местоположений не найдено");
                } else {
                    placeMarkersOnMap(locations);
                }
                datesWindow.close();
            }
        });

        VerticalLayout dates = new VerticalLayout(beforeDateField, afterDateField, submitButton);
        dates.setComponentAlignment(submitButton, Alignment.MIDDLE_CENTER);
        dates.setComponentAlignment(beforeDateField, Alignment.MIDDLE_CENTER);
        dates.setComponentAlignment(afterDateField, Alignment.MIDDLE_CENTER);
        dates.setSpacing(true);

        datesWindow = new Window("Введите даты");
        datesWindow.setContent(dates);
        datesWindow.center();
        Button windowButton = new Button(null, new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                getUI().addWindow(datesWindow);
            }
        });
        windowButton.setIcon(FontAwesome.CALENDAR, "Ввод даты");

        buttonLayoutRow.addComponent(windowButton);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

}
