package kz.gcs.views;

import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import kz.gcs.views.geolocation.GeoLocationView;
import kz.gcs.views.maps.MapView;
import kz.gcs.views.settings.SettingsView;


public enum MenuViewType {
    MAP(MapView.VIEW_NAME, MapView.class, FontAwesome.MAP),
    SETTINGS(SettingsView.VIEW_NAME, SettingsView.class, FontAwesome.WRENCH),
    GEOLOCATION(GeoLocationView.VIEW_NAME, GeoLocationView.class, FontAwesome.MAP_MARKER);

    private final String viewName;
    private final Class<? extends View> viewClass;
    private final Resource icon;

    MenuViewType(String viewName, Class<? extends View> viewClass, Resource icon) {
        this.viewName = viewName;
        this.icon = icon;
        this.viewClass = viewClass;
    }

    public String getViewName() {
        return viewName;
    }

    public Class<? extends View> getViewClass() {
        return viewClass;
    }

    public Resource getIcon() {
        return icon;
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
