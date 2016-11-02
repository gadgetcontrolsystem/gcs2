package kz.gcs.views;

import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import kz.gcs.views.geolocation.GeoLocationView;
import kz.gcs.views.maps.MapView;
import kz.gcs.views.settings.SettingsView;


public enum MenuViewType {
    MAP(MapView.VIEW_NAME,MapView.VIEW_TITLE, MapView.class, FontAwesome.MAP, true),
    SETTINGS(SettingsView.VIEW_NAME, SettingsView.VIEW_TITLE, SettingsView.class, FontAwesome.WRENCH, true),
    GEOLOCATION(GeoLocationView.VIEW_NAME,GeoLocationView.VIEW_TITLE,  GeoLocationView.class, FontAwesome.MAP_MARKER, false);

    private final String viewName;
    private final String viewTitle;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;

    MenuViewType(String viewName, String viewTitle, Class<? extends View> viewClass, Resource icon, boolean stateful) {
        this.viewName = viewName;
        this.viewTitle = viewTitle;
        this.icon = icon;
        this.viewClass = viewClass;
        this.stateful = stateful;
    }

    public String getViewName() {
        return viewName;
    }

    public String getViewTitle() {
        return viewTitle;
    }

    public Class<? extends View> getViewClass() {
        return viewClass;
    }

    public Resource getIcon() {
        return icon;
    }

    public boolean isStateful() {
        return stateful;
    }

    public static MenuViewType getByViewName(final String viewName) {
        MenuViewType result = null;
        for (MenuViewType viewType : values()) {
            if (viewType.getViewName().equals(viewName)) {
                result = viewType;
                break;
            }
        }
        return result;
    }
}
