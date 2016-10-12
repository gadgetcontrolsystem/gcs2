package kz.gcs.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

import javax.annotation.PostConstruct;

@SpringView(name = MainView.VIEW_NAME)
public class MainView extends HorizontalLayout implements View {

    public static final String VIEW_NAME = "mainView";

    @PostConstruct
    void init() {
        setSizeFull();
        addStyleName("mainview");

        addComponent(new MenuView());

        ComponentContainer content = new CssLayout();
        content.addStyleName("view-content");
        content.setSizeFull();
        addComponent(content);
        setExpandRatio(content, 1.0f);

        //getUI().getNavigator().destroy();
        getUI().setNavigator(new Navigator(getUI(), content));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
