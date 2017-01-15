package kz.gcs;

import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.event.UIEvents;
import com.vaadin.server.*;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import kz.gcs.data.DataProvider;
import kz.gcs.data.dummy.DummyDataProvider;
import kz.gcs.data.service.PositionService;
import kz.gcs.data.service.UserService;
import kz.gcs.domain.User;
import kz.gcs.event.DashboardEvent;
import kz.gcs.event.DashboardEvent.BrowserResizeEvent;
import kz.gcs.event.DashboardEvent.CloseOpenWindowsEvent;
import kz.gcs.event.DashboardEvent.UserLoggedOutEvent;
import kz.gcs.event.DashboardEvent.UserLoginRequestedEvent;
import kz.gcs.event.DashboardEventBus;
import kz.gcs.views.LoginView;
import kz.gcs.views.MainView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */

@Component
@Scope("prototype")
@Theme("dashboard")
@Widgetset("kz.gcs.DashboardWidgetSet")
@Title("GCS")
public class MyUI extends UI {

    /*
     * This field stores an access to the dummy backend layer. In real
     * applications you most likely gain access to your beans trough lookup or
     * injection; and not in the UI but somewhere closer to where they're
     * actually accessed.
     */
    private DataProvider dataProvider;
    private final DashboardEventBus dashboardEventbus = new DashboardEventBus();


    @Autowired
    PositionService positionService;


    @Autowired
    UserService userService;

    @Override
    protected void init(final VaadinRequest request) {
        dataProvider = new DummyDataProvider(positionService, userService);

        getPage().getJavaScript().execute("document.head.innerHTML += '<meta name=\"viewport\" content=\"initial-scale = 1.0,maximum-scale = 1.0\">'");

        setLocale(Locale.US);

        DashboardEventBus.register(this);
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        updateContent(false);

        setPollInterval(60000);

        addPollListener(new UIEvents.PollListener() {
            @Override
            public void poll(UIEvents.PollEvent event) {
                DashboardEventBus.post(new DashboardEvent.NotificationsCountUpdatedEvent());
            }
        });

        // Some views need to be aware of browser resize events so a
        // BrowserResizeEvent gets fired to the event bus on every occasion.
        Page.getCurrent().addBrowserWindowResizeListener(
                new Page.BrowserWindowResizeListener() {
                    @Override
                    public void browserWindowResized(
                            final Page.BrowserWindowResizeEvent event) {
                        DashboardEventBus.post(new BrowserResizeEvent());
                    }
                });
    }

    /**
     * Updates the correct content for this UI based on the current user status.
     * If the user is logged in with appropriate privileges, main view is shown.
     * Otherwise login view is shown.
     */
    private void updateContent(boolean showError) {
        User user = (User) VaadinSession.getCurrent().getAttribute(
                User.class.getName());
        if (user != null && user.getAdmin()) {
            // Authenticated user
            setContent(new MainView());

            removeStyleName("loginview");

            getNavigator().navigateTo(getNavigator().getState());
        } else {
            setContent(new LoginView(showError));
            addStyleName("loginview");
        }
    }

    @Subscribe
    public void userLoginRequested(final UserLoginRequestedEvent event) {
        User user = getDataProvider().authenticate(event.getUserName(),
                event.getPassword());
        VaadinSession.getCurrent().setAttribute(User.class.getName(), user);
        updateContent(true);
    }

    @Subscribe
    public void userLoggedOut(final UserLoggedOutEvent event) {
        // When the user logs out, current VaadinSession gets closed and the
        // page gets reloaded on the login screen. Do notice the this doesn't
        // invalidate the current HttpSession.
        VaadinSession.getCurrent().close();
        Page.getCurrent().reload();
    }

    @Subscribe
    public void closeOpenWindows(final CloseOpenWindowsEvent event) {
        for (Window window : getWindows()) {
            window.close();
        }
    }

    /**
     * @return An instance for accessing the (dummy) services layer.
     */
    public static DataProvider getDataProvider() {
        return ((MyUI) getCurrent()).dataProvider;
    }

    public static DashboardEventBus getDashboardEventbus() {
        return ((MyUI) getCurrent()).dashboardEventbus;
    }

}
