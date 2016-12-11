package kz.gcs.views.geolocation;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import kz.gcs.MyUI;
import kz.gcs.domain.Location;
import kz.gcs.event.DashboardEvent;
import kz.gcs.event.DashboardEventBus;
import kz.gcs.util.AllUtils;
import kz.gcs.views.MenuViewType;
import org.vaadin.maddon.FilterableListContainer;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class GeoLocationView extends VerticalLayout implements View {

    public static final String VIEW_TITLE = "геолокация";
    public static final String VIEW_NAME = "locations";
    private static final long serialVersionUID = -4391726112270545081L;

    private final Table table;
    private Button createReport;
    private static final DateFormat DATEFORMAT = new SimpleDateFormat(
            "MM/dd/yyyy hh:mm:ss a");
    private static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#.##");
    private static final String[] DEFAULT_COLLAPSIBLE = { "country", "city", "lat", "lon" };

    public GeoLocationView() {
        setSizeFull();
        addStyleName("transactions");
        DashboardEventBus.register(this);

        addComponent(buildToolbar());

        table = buildTable();
        addComponent(table);
        setExpandRatio(table, 1);
    }

    @Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
        DashboardEventBus.unregister(this);
    }

    private Component buildToolbar() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);
        Responsive.makeResponsive(header);

        Label title = new Label("Список Местоположений");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);

        createReport = buildCreateReport();
        HorizontalLayout tools = new HorizontalLayout(buildFilter(),
                createReport);
        tools.setSpacing(true);
        tools.addStyleName("toolbar");
        header.addComponent(tools);

        return header;
    }

    private Button buildCreateReport() {
        final Button createReport = new Button("Показать на карте");
        createReport
                .setDescription("Отобразить выбранные местоположения на карте");
        createReport.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                createNewReportFromSelection();
            }
        });
        createReport.setEnabled(false);
        return createReport;
    }

    private Component buildFilter() {
        final TextField filter = new TextField();
        filter.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(final FieldEvents.TextChangeEvent event) {
                Container.Filterable data = (Container.Filterable) table.getContainerDataSource();
                data.removeAllContainerFilters();
                data.addContainerFilter(new Container.Filter() {
                    @Override
                    public boolean passesFilter(final Object itemId,
                                                final Item item) {

                        if (event.getText() == null
                                || event.getText().equals("")) {
                            return true;
                        }

                        return filterByProperty("country", item,
                                event.getText())
                                || filterByProperty("city", item,
                                event.getText());

                    }

                    @Override
                    public boolean appliesToProperty(final Object propertyId) {
                        if (propertyId.equals("country")
                                || propertyId.equals("city")
                                ) {
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

        filter.setInputPrompt("Поиск");
        filter.setIcon(FontAwesome.SEARCH);
        filter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        filter.addShortcutListener(new ShortcutListener("Clear",
                ShortcutAction.KeyCode.ESCAPE, null) {
            @Override
            public void handleAction(final Object sender, final Object target) {
                filter.setValue("");
                ((com.vaadin.data.Container.Filterable) table.getContainerDataSource())
                        .removeAllContainerFilters();
            }
        });
        return filter;
    }

    private Table buildTable() {
        final Table table = new Table() {
            @Override
            protected String formatPropertyValue(final Object rowId,
                                                 final Object colId, final Property<?> property) {
                String result = super.formatPropertyValue(rowId, colId,
                        property);
                if (colId.equals("time")) {
                    //result = DATEFORMAT.format(((Date) property.getValue()));
                    result = AllUtils.dateToStrDateTimeP((Date) property.getValue(), "");
                }
                return result;
            }
        };
        table.setSizeFull();
        table.addStyleName(ValoTheme.TABLE_BORDERLESS);
        table.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        table.addStyleName(ValoTheme.TABLE_COMPACT);
        table.setSelectable(true);

        table.setColumnCollapsingAllowed(true);
        table.setColumnCollapsible("time", false);
        table.setColumnCollapsible("city", false);
        table.setColumnCollapsible("lat", false);
        table.setColumnCollapsible("lon", false);

        table.setColumnReorderingAllowed(true);
        table.setContainerDataSource(new TempLocationsContainer(MyUI
                .getDataProvider().getRecentLocations(200)));
        table.setSortContainerPropertyId("time");
        table.setSortAscending(false);


        table.setVisibleColumns("time", "country", "city", "lat",
                "lon");
        table.setColumnHeaders("Время", "Страна", "Город", "Широта",
                "Долгота");



        // Allow dragging items to the reports menu
        table.setDragMode(Table.TableDragMode.MULTIROW);
        table.setMultiSelect(true);

        table.addActionHandler(new LocationsActionHandler());

        table.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                if (table.getValue() instanceof Set) {
                    Set<Object> val = (Set<Object>) table.getValue();
                    createReport.setEnabled(val.size() > 0);
                }
            }
        });
        table.setImmediate(true);

        return table;
    }

    private boolean defaultColumnsVisible() {
        boolean result = true;
        for (String propertyId : DEFAULT_COLLAPSIBLE) {
            if (table.isColumnCollapsed(propertyId) == Page.getCurrent()
                    .getBrowserWindowWidth() < 800) {
                result = false;
            }
        }
        return result;
    }

    @Subscribe
    public void browserResized(final DashboardEvent.BrowserResizeEvent event) {
        // Some columns are collapsed when browser window width gets small
        // enough to make the table fit better.
        if (defaultColumnsVisible()) {
            for (String propertyId : DEFAULT_COLLAPSIBLE) {
                table.setColumnCollapsed(propertyId, Page.getCurrent()
                        .getBrowserWindowWidth() < 800);
            }
        }
    }

    private boolean filterByProperty(final String prop, final Item item,
                                     final String text) {
        if (item == null || item.getItemProperty(prop) == null
                || item.getItemProperty(prop).getValue() == null) {
            return false;
        }
        String val = item.getItemProperty(prop).getValue().toString().trim()
                .toLowerCase();
        if (val.contains(text.toLowerCase().trim())) {
            return true;
        }
        return false;
    }

    void createNewReportFromSelection() {
        UI.getCurrent().getNavigator()
                .navigateTo(MenuViewType.MAP.getViewName());
        DashboardEventBus.post(new DashboardEvent.TransactionReportEvent(
                (Collection<Location>) table.getValue()));
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
    }

    private class LocationsActionHandler implements Action.Handler {
        private final Action report = new Action("Show on map");

        private final Action discard = new Action("Discard");


        @Override
        public void handleAction(final Action action, final Object sender,
                                 final Object target) {
            if (action == report) {
                createNewReportFromSelection();
            } else if (action == discard) {
                Notification.show("Not implemented in this demo");
            }
        }

        @Override
        public Action[] getActions(final Object target, final Object sender) {
            return new Action[] { report, discard };
        }
    }

    private class TempLocationsContainer extends
            FilterableListContainer<Location> {

        public TempLocationsContainer(
                final Collection<Location> collection) {
            super(collection);
        }

        // This is only temporarily overridden until issues with
        // BeanComparator get resolved.
        @Override
        public void sort(final Object[] propertyId, final boolean[] ascending) {
            final boolean sortAscending = ascending[0];
            final Object sortContainerPropertyId = propertyId[0];
            Collections.sort(getBackingList(), new Comparator<Location>() {
                @Override
                public int compare(final Location o1, final Location o2) {
                    int result = 0;
                    if ("time".equals(sortContainerPropertyId)) {
                        result = o1.getTime().compareTo(o2.getTime());
                    } else if ("country".equals(sortContainerPropertyId)) {
                        result = o1.getCountry().compareTo(o2.getCountry());
                    } else if ("city".equals(sortContainerPropertyId)) {
                        result = o1.getCity().compareTo(o2.getCity());
                    } else if ("lat".equals(sortContainerPropertyId)) {
                        result = o1.getLat().compareTo(o2.getLat());
                    } else if ("lon".equals(sortContainerPropertyId)) {
                        result = o1.getLon().compareTo(o2.getLon());
                    }

                    if (!sortAscending) {
                        result *= -1;
                    }
                    return result;
                }
            });
        }

    }
}
