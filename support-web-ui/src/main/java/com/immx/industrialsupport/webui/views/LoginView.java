package com.immx.industrialsupport.webui.views;

import com.immx.industrialsupport.client.AuthenticationClient;
import com.immx.industrialsupport.contracts.authorization.LoginResponse;
import com.immx.industrialsupport.webui.session.UserSession;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.util.UUID;

@Route("login")
@PageTitle("Авторизация")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    @Autowired
    private AuthenticationClient authenticationClient;

    @Autowired
    private UserSession userSession;

    private final TextField departmentIdField = new TextField("Department ID");

    private final TextField usernameField = new TextField("Логин");

    private final PasswordField passwordField = new PasswordField("Пароль");

    private final Button loginButton = new Button("Войти");

    public LoginView() {
        configureLayout();
        configureFields();
        configureButton();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(userSession.isAuthenticated())
            event.forwardTo(MainView.class);
    }

    private void configureLayout() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        VerticalLayout loginPanel = new VerticalLayout();

        loginPanel.setWidth("400px");
        loginPanel.setPadding(true);
        loginPanel.setSpacing(true);
        loginPanel.setAlignItems(Alignment.STRETCH);

        loginPanel.add(
                new H1("Industrial Support Hub"),
                departmentIdField,
                usernameField,
                passwordField,
                loginButton
        );

        add(loginPanel);
    }

    private void configureFields() {
        departmentIdField.setRequired(true);
        departmentIdField.setPlaceholder(
                "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx");
        departmentIdField.setAutofocus(true);

        usernameField.setRequired(true);
        passwordField.setRequired(true);
    }

    private void configureButton() {
        loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        loginButton.addClickShortcut(Key.ENTER);
        loginButton.addClickListener(event -> login());
    }

    private void login() {
        if(departmentIdField.isEmpty()
           || usernameField.isEmpty()
           || passwordField.isEmpty()) {
            authenticationFailed("Заполните все поля");
            return;
        }

        UUID departmentId;

        try {
            departmentId = UUID.fromString(departmentIdField.getValue()
                    .trim());
        } catch(IllegalArgumentException ex) {
            authenticationFailed("Некорректный идентификатор подразделения");
            return;
        }

        loginButton.setEnabled(false);

        try {
            LoginResponse response = authenticationClient.login(
                    departmentId,
                    usernameField.getValue()
                            .trim(),
                    passwordField.getValue());

            userSession.authenticate(response);

            UI.getCurrent()
                    .navigate(MainView.class);
        } catch(RestClientResponseException ex) {
            authenticationFailed("Неверный Department ID, логин или пароль");
        } catch(RestClientException ex) {
            authenticationFailed("Support Service недоступен");
        } finally {
            loginButton.setEnabled(true);
        }
    }

    private void authenticationFailed(String message) {
        departmentIdField.clear();
        usernameField.clear();
        passwordField.clear();

        departmentIdField.focus();

        Notification.show(
                message,
                4000,
                Notification.Position.MIDDLE);
    }
}
