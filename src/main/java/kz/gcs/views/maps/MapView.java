package kz.gcs.views.maps;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Property;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import kz.gcs.MyUI;
import kz.gcs.domain.Command;
import kz.gcs.domain.CommandParameter;
import kz.gcs.domain.Position;
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

import java.util.*;


public class MapView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "map";
    public static final String VIEW_TITLE = "карта";
    private static final long serialVersionUID = -379716808231511716L;

    private Window datesWindow;
    private Window commandsWindow;

    private GoogleMap googleMap;


    private final String apiKey = "AIzaSyCJccmv1mCCRdsG8ubD2wcMA7zKEoty5pc";

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

        Position position = MyUI.getDataProvider().getLastLocation();
        if (position != null) {
            googleMap = new GoogleMap(new LatLon(position.getLatitude(), position.getLongitude()), 15, this.apiKey);
        } else {
            googleMap = new GoogleMap(new LatLon(51.1279879, 71.4317533), 15, this.apiKey);
        }
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
        List<Position> positions = new ArrayList<>(event.getLocations());
        if (positions.size() == 0)
            return;
        Collections.sort(positions);
        placeMarkersOnMap(positions);
        googleMap.setZoom(15);
        DashboardEventBus.post(new DashboardEvent.ReportsCountUpdatedEvent(
                getComponentCount() - 1));
    }

    private void placeMarkersOnMap(List<Position> locations) {
        List<LatLon> latLons = new ArrayList<>();
        String url = "http://mt.google.com/vt/icon/name=icons/spotlight/measle_green_8px.png&scale=2";
        int counter = 1;
        LatLon maxLatLon = new LatLon(0, 0);
        LatLon minLatLon = new LatLon(90, 90);
        for (Position temp : locations) {
            LatLon latLon = new LatLon(temp.getLatitude(), temp.getLongitude());
            latLons.add(latLon);

            if (maxLatLon.getLat() < temp.getLatitude()) {
                maxLatLon.setLat(temp.getLatitude());
            }
            if (maxLatLon.getLon() < temp.getLongitude()) {
                maxLatLon.setLon(temp.getLongitude());
            }
            if (minLatLon.getLat() > temp.getLatitude()) {
                minLatLon.setLat(temp.getLatitude());
            }
            if (minLatLon.getLon() > temp.getLongitude()) {
                minLatLon.setLon(temp.getLongitude());
            }

            if (counter == 1) {
                url = "http://mt.google.com/vt/icon?color=ff004C13&name=icons/spotlight/spotlight-waypoint-a.png";
            } else if (counter == locations.size()) {
                url = "http://mt.google.com/vt/icon?color=ff004C13&name=icons/spotlight/spotlight-waypoint-b.png";
            }

            GoogleMapMarker marker = new GoogleMapMarker(temp.displayStr(), latLon, false, url);
            googleMap.addMarker(marker);

            GoogleMapCircle mapCircle = new GoogleMapCircle(latLon, temp.getAccuracy());
            googleMap.addCircleOverlay(mapCircle);
            GoogleMapInfoWindow window = new GoogleMapInfoWindow(AllUtils.dateToStrDateTimeP(temp.getDeviceTime(), "Время не доступно") + " Lat: " + temp.getLatitude() + " Lon: " + temp.getLongitude(), marker);
            OpenInfoWindowOnMarkerClickListener windowOpener = new OpenInfoWindowOnMarkerClickListener(googleMap, marker, window);
            googleMap.addMarkerClickListener(windowOpener);

            counter++;
        }
        googleMap.fitToBounds(maxLatLon, minLatLon);

        GoogleMapPolyline mapPolyline = new GoogleMapPolyline(latLons, "#2e96db", 0.8, 2);
        googleMap.addPolyline(mapPolyline);
        Position latest = locations.get(locations.size() - 1);
        googleMap.setCenter(new LatLon(latest.getLatitude(), latest.getLongitude()));
    }

    private void getLastLocation() {


        Position lastLocation = MyUI.getDataProvider().getLastLocation();
        if (lastLocation != null) {
            googleMap.clearAll();
            LatLon position = new LatLon(lastLocation.getLatitude(), lastLocation.getLongitude());
            GoogleMapMarker marker = new GoogleMapMarker(lastLocation.displayStr(), position, false, "http://mt.google.com/vt/icon?color=ff004C13&name=icons/spotlight/spotlight-waypoint-b.png");
            googleMap.addMarker(marker);
            googleMap.setCenter(position);
            GoogleMapCircle mapCircle = new GoogleMapCircle(position, lastLocation.getAccuracy());
            googleMap.addCircleOverlay(mapCircle);

            GoogleMapInfoWindow window = new GoogleMapInfoWindow(lastLocation.getContent(), marker);
            OpenInfoWindowOnMarkerClickListener windowOpener = new OpenInfoWindowOnMarkerClickListener(googleMap, marker, window);
            googleMap.addMarkerClickListener(windowOpener);
            googleMap.openInfoWindow(window);
        }
        googleMap.setZoom(15);
    }

    @SuppressWarnings("all")
    private void drawButtons(HorizontalLayout buttonLayoutRow) {

        Button refreshButton = new Button(null, new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                googleMap.clearAll();
                Map attrs = new HashMap<String, Object>();
                MyUI.getDataProvider().sendCommand(Command.TYPE_POSITION_SINGLE.getCommandString(), attrs);
                getLastLocation();
            }
        });
        refreshButton.setIcon(FontAwesome.REFRESH, "Обновить карту");
        buttonLayoutRow.addComponent(refreshButton);


        initCalendarWindow();
        Button windowButton = new Button(null, new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                getUI().addWindow(datesWindow);
            }
        });
        windowButton.setIcon(FontAwesome.CALENDAR, "Ввод даты");
        buttonLayoutRow.addComponent(windowButton);


        initCommandWindow();
        Button sendCommandButton = new Button(null, new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                getUI().addWindow(commandsWindow);
            }
        });
        sendCommandButton.setIcon(FontAwesome.UPLOAD, "Отправить команду");
        buttonLayoutRow.addComponent(sendCommandButton);


    }

    private void initCalendarWindow() {
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
                List<Position> locations = new ArrayList(MyUI.getDataProvider().getLocationsBetween(beforeDate, afterDate));
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
        dates.setMargin(true);
        dates.setSpacing(true);

        datesWindow = new Window("Введите даты");
        datesWindow.setContent(dates);
        datesWindow.setModal(true);
        datesWindow.center();
    }

    private void initCommandWindow() {


        final TextField textField = new TextField("Данные");
        textField.setVisible(false);
        textField.setSizeFull();

        final NativeSelect commandBox = new NativeSelect("Тип", Command.getValues(false));
        commandBox.setSizeFull();
        commandBox.setRequired(true);

        commandBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (commandBox.getValue() != null) {
                    Command selected = (Command) commandBox.getValue();
                    textField.clear();
                    if(selected.equals(Command.TYPE_CUSTOM) || selected.equals(Command.TYPE_SET_INDICATOR) || selected.equals(Command.TYPE_SHOW_MESSAGE)) {
                        textField.setVisible(true);
                    }
                    else {
                        textField.setVisible(false);
                    }
                }
            }
        });


        Button submitButton = new Button("Отправить", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                if (!commandBox.isValid()) {
                    Notification.show("Выберите команду", Notification.Type.WARNING_MESSAGE);
                    return;
                }
                Map<String, Object> attributes = new HashMap<String, Object>();
                Command selected = (Command) commandBox.getValue();
                switch (selected) {
                    case TYPE_CUSTOM:
                        attributes.put(CommandParameter.KEY_DATA.getParameterString(), textField.getValue());
                        break;
                    case TYPE_SET_INDICATOR:
                        attributes.put(CommandParameter.KEY_DATA.getParameterString(), textField.getValue());
                        break;
                    case TYPE_SHOW_MESSAGE:
                        attributes.put(CommandParameter.KEY_MESSAGE.getParameterString(), textField.getValue());
                        break;
                }
                System.out.println();
                System.out.println("Attributes");
                System.out.println(attributes);
                System.out.println();
                MyUI.getDataProvider().sendCommand(((Command) (commandBox.getValue())).getCommandString(), attributes);
                commandsWindow.close();
            }
        });


        Button cancelButton = new Button("Отмена", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                commandsWindow.close();
            }
        });

        HorizontalLayout btnsHL = new HorizontalLayout(submitButton, cancelButton);
        btnsHL.setMargin(true);
        btnsHL.setSpacing(true);
        VerticalLayout dates = new VerticalLayout(commandBox, textField, btnsHL);
        dates.setComponentAlignment(btnsHL, Alignment.MIDDLE_CENTER);
        dates.setComponentAlignment(commandBox, Alignment.MIDDLE_CENTER);
        dates.setComponentAlignment(textField, Alignment.MIDDLE_CENTER);
        dates.setMargin(true);
        dates.setSpacing(true);

        commandsWindow = new Window("Отправить команду");
        commandsWindow.setContent(dates);
        commandsWindow.setModal(true);
        commandsWindow.setWidthUndefined();
        commandsWindow.center();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

}
