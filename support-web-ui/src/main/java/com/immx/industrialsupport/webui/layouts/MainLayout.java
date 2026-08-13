package com.immx.industrialsupport.webui.layouts;

import com.immx.industrialsupport.webui.session.UserSession;
import com.immx.industrialsupport.webui.views.ActiveIncidentsView;
import com.immx.industrialsupport.webui.views.CreateIncidentView;
import com.immx.industrialsupport.webui.views.LoginView;
import com.immx.industrialsupport.webui.views.ProfileView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import org.springframework.beans.factory.annotation.Autowired;

public class MainLayout extends AppLayout implements BeforeEnterObserver {

    @Autowired
    private UserSession userSession;

    private final Button createIncidentButton = new Button("Создать обращение");

    private final Button activeIncidentsButton = new Button("Активные обращения");

    private final Button profileButton = new Button("Личный кабинет");

    private final Button logoutButton = new Button("Выйти");

    private final Span usernameBadge = new Span();

    public MainLayout() {
        configureNavigation();
        configureUsernameBadge();
        configureLogoutButton();

        addToNavbar(createHeader());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(!userSession.isAuthenticated()) {
            event.rerouteTo(LoginView.class);
            return;
        }

        usernameBadge.setText("@" + userSession.getUsername());

        updateActiveButton(event.getLocation()
                .getPath());
    }

    private void configureNavigation() {
        configureNavigationButton(
                createIncidentButton,
                CreateIncidentView.class);

        configureNavigationButton(
                activeIncidentsButton,
                ActiveIncidentsView.class);

        configureNavigationButton(
                profileButton,
                ProfileView.class);
    }

    private void configureNavigationButton(Button button,
                                           Class<? extends Component> view) {
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        button.addClickListener(event -> UI.getCurrent()
                .navigate(view));
    }

    private void configureLogoutButton() {
        logoutButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        logoutButton.addClickListener(event -> {
            userSession.logout();

            UI.getCurrent()
                    .navigate(LoginView.class);
        });
    }

    private HorizontalLayout createHeader() {
        H2 applicationTitle = new H2("Industrial Support Hub");

        applicationTitle.getStyle()
                .set(
                        "margin",
                        "0")
                .set(
                        "white-space",
                        "nowrap");

        HorizontalLayout navigation = new HorizontalLayout(
                createIncidentButton,
                activeIncidentsButton,
                profileButton);

        navigation.setAlignItems(HorizontalLayout.Alignment.CENTER);
        navigation.setSpacing(false);

        HorizontalLayout leftSection = new HorizontalLayout(
                applicationTitle,
                navigation);

        leftSection.setAlignItems(HorizontalLayout.Alignment.CENTER);
        leftSection.setSpacing(true);

        HorizontalLayout userActions = new HorizontalLayout(
                usernameBadge,
                logoutButton);

        userActions.setAlignItems(HorizontalLayout.Alignment.CENTER);

        HorizontalLayout header = new HorizontalLayout(
                leftSection,
                userActions);

        header.setWidthFull();
        header.setPadding(true);
        header.setAlignItems(HorizontalLayout.Alignment.CENTER);
        header.setJustifyContentMode(HorizontalLayout.JustifyContentMode.BETWEEN);

        header.getStyle()
                .set(
                        "box-shadow",
                        "0 1px 4px rgba(0, 0, 0, 0.15)")
                .set(
                        "box-sizing",
                        "border-box");

        return header;
    }

    private void configureUsernameBadge() {
        usernameBadge.getStyle()
                .set(
                        "padding",
                        "var(--lumo-space-xs) var(--lumo-space-m)")
                .set(
                        "border",
                        "1px solid var(--lumo-primary-color-50pct)")
                .set(
                        "border-radius",
                        "var(--lumo-border-radius-m)")
                .set(
                        "background-color",
                        "var(--lumo-primary-color-10pct)")
                .set(
                        "color",
                        "var(--lumo-primary-text-color)")
                .set(
                        "font-weight",
                        "600");
    }

    private void updateActiveButton(String path) {
        setActive(
                createIncidentButton,
                path.equals("incidents/new"));

        setActive(
                activeIncidentsButton,
                path.equals("incidents"));

        setActive(
                profileButton,
                path.equals("profile"));
    }

    private void setActive(Button button,
                           boolean active) {
        if(active) {
            button.removeThemeVariants(ButtonVariant.LUMO_TERTIARY);

            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        } else {
            button.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);

            button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        }
    }
}
