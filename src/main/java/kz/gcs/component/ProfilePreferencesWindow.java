package kz.gcs.component;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.shared.ui.datefield.Resolution;
import kz.gcs.MyUI;
import kz.gcs.domain.User;
import kz.gcs.event.DashboardEvent.CloseOpenWindowsEvent;
import kz.gcs.event.DashboardEvent.ProfileUpdatedEvent;
import kz.gcs.event.DashboardEventBus;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.*;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ProfilePreferencesWindow extends Window implements Serializable {

    public static final String ID = "profilepreferenceswindow";
    private static final long serialVersionUID = -1753338186626916476L;

    private final BeanFieldGroup<User> fieldGroup;
    /*
     * Fields for editing the User object are defined here as class members.
     * They are later bound to a FieldGroup by calling
     * fieldGroup.bindMemberFields(this). The Fields' values don't need to be
     * explicitly set, calling fieldGroup.setItemDataSource(user) synchronizes
     * the fields with the user object.
     */
    @PropertyId("name")
    private TextField firstNameField;
    @PropertyId("surname")
    private TextField lastNameField;
    @PropertyId("title")
    private ComboBox titleField;
    @PropertyId("male")
    private OptionGroup sexField;
    @PropertyId("email")
    private TextField emailField;
    @PropertyId("address")
    private TextField locationField;
    @PropertyId("phone")
    private TextField phoneField;
    /*@PropertyId("newsletterSubscription")
    private OptionalSelect<Integer> newsletterField;*/
//    @PropertyId("website")
//    private TextField websiteField;
//    @PropertyId("bio")
//    private TextArea bioField;

    private Window warningWindow;

    private ProfilePreferencesWindow(final User user,
                                     final boolean preferencesTabOpen) {
        addStyleName("profile-window");
        setId(ID);
        Responsive.makeResponsive(this);

        setModal(true);
        addCloseShortcut(KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(false);
        setHeight(90.0f, Unit.PERCENTAGE);

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setMargin(new MarginInfo(true, false, false, false));
        setContent(content);

        TabSheet detailsWrapper = new TabSheet();
        detailsWrapper.setSizeFull();
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
        content.addComponent(detailsWrapper);
        content.setExpandRatio(detailsWrapper, 1f);

        detailsWrapper.addComponent(buildProfileTab());
        detailsWrapper.addComponent(buildPreferencesTab());

        if (preferencesTabOpen) {
            detailsWrapper.setSelectedTab(1);
        }

        content.addComponent(buildFooter());

        fieldGroup = new BeanFieldGroup<User>(User.class);
        fieldGroup.bindMemberFields(this);
        fieldGroup.setItemDataSource(user);
    }

    private Component buildPreferencesTab() {
        VerticalLayout root = new VerticalLayout();
        root.setCaption("Карта");
        root.setIcon(FontAwesome.MAP);
        root.setSpacing(true);
        root.setMargin(true);
        root.setSizeFull();

        Label message = new Label("Страница находится в разработке");
        message.setSizeUndefined();
        message.addStyleName(ValoTheme.LABEL_LIGHT);
        root.addComponent(message);
        root.setComponentAlignment(message, Alignment.MIDDLE_CENTER);

        return root;
    }

    private Component buildProfileTab() {
        HorizontalLayout root = new HorizontalLayout();
        root.setCaption("Профиль");
        root.setIcon(FontAwesome.USER);
        root.setWidth(100.0f, Unit.PERCENTAGE);
        root.setSpacing(true);
        root.setMargin(true);
        root.addStyleName("profile-form");

        VerticalLayout pic = new VerticalLayout();
        pic.setSizeUndefined();
        pic.setSpacing(true);
        Image profilePic = new Image(null, new ThemeResource(
                "img/profile-pic-300px.jpg"));
        profilePic.setWidth(100.0f, Unit.PIXELS);
        pic.addComponent(profilePic);

        Button upload = new Button("Изменить…", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                Notification.show("Страница находится в разработке");
            }
        });
        upload.addStyleName(ValoTheme.BUTTON_TINY);
        pic.addComponent(upload);

        root.addComponent(pic);

        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        root.addComponent(details);
        root.setExpandRatio(details, 1);

        firstNameField = new TextField("Имя");
        details.addComponent(firstNameField);
        lastNameField = new TextField("Фамилия");
        details.addComponent(lastNameField);

        /*titleField = new ComboBox("Title");
        titleField.setInputPrompt("Please specify");
        titleField.addItem("Mr.");
        titleField.addItem("Mrs.");
        titleField.addItem("Ms.");
        titleField.setNewItemsAllowed(true);
        details.addComponent(titleField);*/

        sexField = new OptionGroup("Пол");
        sexField.addItem(Boolean.FALSE);
        sexField.setItemCaption(Boolean.FALSE, "Женский");
        sexField.addItem(Boolean.TRUE);
        sexField.setItemCaption(Boolean.TRUE, "Мужской");
        sexField.addStyleName("horizontal");
        details.addComponent(sexField);

        Label section = new Label("Контакты");
        section.addStyleName(ValoTheme.LABEL_H4);
        section.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(section);

        emailField = new TextField("Email");
        emailField.setWidth("100%");
        emailField.setRequired(true);
        emailField.setNullRepresentation("");
        details.addComponent(emailField);

        locationField = new TextField("Адрес");
        locationField.setWidth("100%");
        locationField.setNullRepresentation("");
        locationField.setComponentError(new UserError(
                "Данный адрес не зарегистрирован в системе"));
        details.addComponent(locationField);

        phoneField = new TextField("Тел.");
        phoneField.setWidth("100%");
        phoneField.setNullRepresentation("");
        details.addComponent(phoneField);

        /*newsletterField = new OptionalSelect<Integer>();
        newsletterField.addOption(0, "Daily");
        newsletterField.addOption(1, "Weekly");
        newsletterField.addOption(2, "Monthly");
        details.addComponent(newsletterField);*/

        section = new Label("Доп. информация");
        section.addStyleName(ValoTheme.LABEL_H4);
        section.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(section);

//        websiteField = new TextField("Вебсайт");
//        websiteField.setInputPrompt("http://");
//        websiteField.setWidth("100%");
//        websiteField.setNullRepresentation("");
//        details.addComponent(websiteField);
//
//        bioField = new TextArea("Биография");
//        bioField.setWidth("100%");
//        bioField.setRows(4);
//        bioField.setNullRepresentation("");
//        details.addComponent(bioField);

        return root;
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        Button cancel = new Button("Закрыть");
        initWarningWindow();
        //cancel.addStyleName(ValoTheme.BUTTON);
        cancel.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                getUI().addWindow(warningWindow);
            }
        });
        footer.addComponent(cancel);
        footer.setComponentAlignment(cancel, Alignment.MIDDLE_CENTER);

        Button ok = new Button("Применить");
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    fieldGroup.commit();
                    // Updated user should also be persisted to database. But
                    // not in this demo.

                    Notification success = new Notification(
                            "Настройки изменены");
                    success.setDelayMsec(2000);
                    success.setStyleName("bar success small");
                    success.setPosition(Position.BOTTOM_CENTER);
                    success.show(Page.getCurrent());

                    DashboardEventBus.post(new ProfileUpdatedEvent());
                    close();
                } catch (CommitException e) {
                    Notification.show("Ошибка при изменении настроек",
                            Type.ERROR_MESSAGE);
                }

            }
        });
        ok.focus();
        footer.addComponent(ok);
        footer.setComponentAlignment(ok, Alignment.MIDDLE_CENTER);
        return footer;
    }

    private void initWarningWindow() {
        Label warningLabel = new Label("Вы действительно хотите закрыть окно настроек?");
        Label warningLabel2 = new Label("Все не сохраненные изменения будут потеряны!");
        //warningLabel.addStyleName();

        Button yesButton = new Button("Да", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                warningWindow.close();
                close();
            }
        });
        Button noButton = new Button("Нет", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                warningWindow.close();
            }
        });

        HorizontalLayout buttonLayout = new HorizontalLayout(yesButton, noButton);
        VerticalLayout warningLayout = new VerticalLayout(warningLabel, warningLabel2, buttonLayout);
        warningLayout.setComponentAlignment(warningLabel, Alignment.MIDDLE_CENTER);
        warningLayout.setComponentAlignment(warningLabel2, Alignment.MIDDLE_CENTER);
        warningLayout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_CENTER);

        buttonLayout.setComponentAlignment(yesButton, Alignment.MIDDLE_LEFT);
        buttonLayout.setComponentAlignment(noButton, Alignment.MIDDLE_RIGHT);

        buttonLayout.setMargin(true);
        buttonLayout.setSpacing(true);
        warningLayout.setMargin(true);
        warningLayout.setSpacing(true);

        warningWindow = new Window("Выберите действие:");
        warningWindow.setContent(warningLayout);
        warningWindow.setModal(true);
        warningWindow.center();
    }

    public static void open(final User user, final boolean preferencesTabActive) {
        DashboardEventBus.post(new CloseOpenWindowsEvent());
        Window w = new ProfilePreferencesWindow(user, preferencesTabActive);
        UI.getCurrent().addWindow(w);
        w.focus();
    }
}
