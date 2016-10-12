package kz.gcs;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.EnableVaadin;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.spring.server.SpringVaadinServlet;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("dashboard")
@SpringUI
public class MyUI extends UI {

    private static boolean loggedIn;

    @Autowired
    private SpringViewProvider viewProvider;

    @WebServlet(value = "/*", asyncSupported = true)
    public static class Servlet extends SpringVaadinServlet {
    }

    @WebListener
    public static class MyContextLoaderListener extends ContextLoaderListener {
    }

    public MyUI() {
    }

    @Configuration
    @EnableVaadin
    public static class MyConfiguration {

    }

    @Override
    protected void init(VaadinRequest request) {

        setLoggedIn(false);

        /*Navigator navigator = new Navigator(this, this);
        navigator.addProvider(viewProvider);*/

        getNavigator().navigateTo("");

    }

    public static void setLoggedIn(boolean loggedIn) {
        MyUI.loggedIn = loggedIn;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

}
