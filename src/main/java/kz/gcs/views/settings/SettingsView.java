package kz.gcs.views.settings;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = SettingsView.VIEW_NAME)
public class SettingsView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "settingsView";

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
