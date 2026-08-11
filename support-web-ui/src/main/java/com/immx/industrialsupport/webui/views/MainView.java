package com.immx.industrialsupport.webui.views;

import com.immx.industrialsupport.webui.session.UserSession;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
@PageTitle("Main Page")
public class MainView extends VerticalLayout implements BeforeEnterObserver {

    @Autowired
    private UserSession userSession;

    private final Button logoutButton = new Button("Выйти");

    private final Span usernameBadge = new Span();

    public MainView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        configureLogoutButton();
        configureUsernameBadge();

        HorizontalLayout header = createHeader();

        VerticalLayout content = new VerticalLayout(new H1("Main Page"));

        content.setWidthFull();
        content.setAlignItems(Alignment.CENTER);
        content.setJustifyContentMode(JustifyContentMode.CENTER);

        add(
                header,
                content);

        expand(content);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(!userSession.isAuthenticated()) {
            event.rerouteTo(LoginView.class);
            return;
        }

        usernameBadge.setText("@" + userSession.getUsername());
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
                        "0");

        HorizontalLayout userActions = new HorizontalLayout(
                usernameBadge,
                logoutButton);

        userActions.setAlignItems(Alignment.CENTER);
        userActions.setSpacing(true);

        HorizontalLayout header = new HorizontalLayout(
                applicationTitle,
                userActions);

        header.setWidthFull();
        header.setPadding(true);

        header.setAlignItems(Alignment.CENTER);
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);

        header.getStyle()
                .set(
                        "box-shadow",
                        "0 1px 4px rgba(0, 0, 0, 0.15)");

        return header;
    }

    private void configureUsernameBadge() {
        usernameBadge.getStyle()
                .set(
                        "padding",
                        "var(--lumo-space-xs) var (--lumo-space-m)")
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
}
