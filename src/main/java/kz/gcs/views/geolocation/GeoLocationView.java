package kz.gcs.views.geolocation;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = GeoLocationView.VIEW_NAME)
public class GeoLocationView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "geoLocationView";
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
