package com.immx.industrialsupport.webui.views;

import com.immx.industrialsupport.client.UserClient;
import com.immx.industrialsupport.contracts.common.IndustrialSupportResponseData;
import com.immx.industrialsupport.contracts.role.RoleName;
import com.immx.industrialsupport.contracts.user.UserResponse;
import com.immx.industrialsupport.webui.layouts.MainLayout;
import com.immx.industrialsupport.webui.session.UserSession;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Locale;

@Route(
        value = "profile",
        layout = MainLayout.class
)
@PageTitle("Личный кабинет")
public class ProfileView extends VerticalLayout implements BeforeEnterObserver {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private static final DateTimeFormatter SESSION_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

    private static final String CONTENT_MAX_WIDTH = "1100px";

    private final UserClient userClient;

    private final UserSession userSession;

    private final Div avatarValue = new Div();

    private final H2 fullNameValue = new H2();

    private final Span usernameValue = new Span();

    private final Span statusValue = new Span();

    private final Div rolesValue = new Div();

    private final Span emailValue = createValueSpan();

    private final Span organizationValue = createValueSpan();

    private final Span departmentValue = createValueSpan();

    private final Span telegramValue = createValueSpan();

    private final Span externalIdValue = createValueSpan();

    private final Span createdAtValue = createValueSpan();

    private final Span updatedAtValue = createValueSpan();

    private final Span sessionExpirationValue = new Span();

    /**
     * Конструктор класса {@link ProfileView}.
     *
     * @param userClient  клиент для работы с пользователями
     * @param userSession авторизованная сессия текущего пользователя
     */
    public ProfileView(UserClient userClient,
                       UserSession userSession) {
        this.userClient = userClient;
        this.userSession = userSession;

        configurePage();

        add(
                createHeader(),
                createOverviewCard(),
                createDetails(),
                createSessionCard());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(!userSession.isAuthenticated()) {
            event.forwardTo(LoginView.class);
            return;
        }

        loadProfile();
    }

    private void configurePage() {
        setWidthFull();
        setPadding(true);
        setSpacing(true);
        setAlignItems(Alignment.CENTER);

        getStyle().set(
                "box-sizing",
                "border-box");
    }

    private Div createHeader() {
        H1 title = new H1("Личный кабинет");

        title.getStyle()
                .set(
                        "margin",
                        "0");

        Paragraph subtitle = new Paragraph("Персональные данные и параметры учетной записи");

        subtitle.getStyle()
                .set(
                        "margin",
                        "var(--vaadin-gap-xs) 0 0")
                .set(
                        "color",
                        "var(--vaadin-text-color-secondary)");

        Div header = new Div();

        header.add(
                title,
                subtitle);

        header.setWidthFull();

        header.getStyle()
                .set(
                        "max-width",
                        CONTENT_MAX_WIDTH)
                .set(
                        "margin-inline",
                        "auto");

        return header;
    }

    private VerticalLayout createOverviewCard() {
        configureAvatar();
        configureIdentityValues();
        configureStatus();

        Div nameContainer = new Div();

        nameContainer.add(
                fullNameValue,
                usernameValue);

        nameContainer.getStyle()
                .set(
                        "min-width",
                        "0");

        HorizontalLayout identity = new HorizontalLayout(
                avatarValue,
                nameContainer);

        identity.setAlignItems(Alignment.CENTER);
        identity.setSpacing(true);

        identity.getStyle()
                .set(
                        "min-width",
                        "0")
                .set(
                        "flex",
                        "1 1 420px");

        HorizontalLayout overviewRow = new HorizontalLayout(
                identity,
                statusValue);

        overviewRow.setWidthFull();
        overviewRow.setAlignItems(Alignment.CENTER);
        overviewRow.setJustifyContentMode(JustifyContentMode.BETWEEN);

        overviewRow.getStyle()
                .set(
                        "flex-wrap",
                        "wrap")
                .set(
                        "gap",
                        "var(--vaadin-gap-l)");

        Span rolesLabel = new Span("Роли и права доступа");

        rolesLabel.getStyle()
                .set(
                        "display",
                        "block")
                .set(
                        "margin-bottom",
                        "var(--vaadin-gap-s)")
                .set(
                        "color",
                        "var(--vaadin-text-color-secondary)")
                .set(
                        "font-size",
                        "14px");

        rolesValue.getStyle()
                .set(
                        "display",
                        "flex")
                .set(
                        "flex-wrap",
                        "wrap")
                .set(
                        "gap",
                        "var(--vaadin-gap-s)");

        Div rolesContainer = new Div();

        rolesContainer.add(
                rolesLabel,
                rolesValue);

        VerticalLayout card = createCard();

        card.setSpacing(true);
        card.add(
                overviewRow,
                rolesContainer);

        card.getStyle()
                .set(
                        "max-width",
                        CONTENT_MAX_WIDTH)
                .set(
                        "margin-inline",
                        "auto");

        return card;
    }

    private void configureAvatar() {
        avatarValue.setText("--");

        avatarValue.getStyle()
                .set(
                        "display",
                        "grid")
                .set(
                        "place-items",
                        "center")
                .set(
                        "flex",
                        "0 0 auto")
                .set(
                        "width",
                        "88px")
                .set(
                        "height",
                        "88px")
                .set(
                        "border-radius",
                        "50%")
                .set(
                        "background",
                        "var(--aura-accent-color)")
                .set(
                        "color",
                        "var(--aura-accent-contrast-color)")
                .set(
                        "font-size",
                        "28px")
                .set(
                        "font-weight",
                        "700")
                .set(
                        "box-shadow",
                        "0 0 0 5px var(--aura-accent-surface)");
    }

    private void configureIdentityValues() {
        fullNameValue.setText("Загрузка...");

        fullNameValue.getStyle()
                .set(
                        "margin",
                        "0")
                .set(
                        "overflow-wrap",
                        "anywhere");

        usernameValue.setText("-");

        usernameValue.getStyle()
                .set(
                        "display",
                        "block")
                .set(
                        "margin-top",
                        "var(--vaadin-gap-xs)")
                .set(
                        "color",
                        "var(--vaadin-text-color-secondary)")
                .set(
                        "overflow-wrap",
                        "anywhere");
    }

    private void configureStatus() {
        statusValue.setText("Загрузка...");

        statusValue.getStyle()
                .set(
                        "padding",
                        "var(--vaadin-padding-xs) var(--vaadin-padding-m)")
                .set(
                        "border",
                        "1px solid")
                .set(
                        "border-radius",
                        "999px")
                .set(
                        "font-size",
                        "14px")
                .set(
                        "font-weight",
                        "600")
                .set(
                        "white-space",
                        "nowrap")
                .set(
                        "flex",
                        "0 0 auto");
    }

    private HorizontalLayout createDetails() {
        VerticalLayout personalDataCard = createInformationCard(
                "Основная информация",
                createInformationRow(
                        "Электронная почта",
                        emailValue),
                createInformationRow(
                        "Организация",
                        organizationValue),
                createInformationRow(
                        "Подразделение",
                        departmentValue));

        VerticalLayout serviceDataCard = createInformationCard(
                "Служебная информация",
                createInformationRow(
                        "Telegram",
                        telegramValue),
                createInformationRow(
                        "Внутренний идентификатор",
                        externalIdValue),
                createInformationRow(
                        "Дата регистрации",
                        createdAtValue),
                createInformationRow(
                        "Последнее обновление",
                        updatedAtValue));

        personalDataCard.getStyle()
                .set(
                        "width",
                        "auto")
                .set(
                        "min-width",
                        "0")
                .set(
                        "flex",
                        "1 1 420px");

        serviceDataCard.getStyle()
                .set(
                        "width",
                        "auto")
                .set(
                        "min-width",
                        "0")
                .set(
                        "flex",
                        "1 1 420px");

        HorizontalLayout details = new HorizontalLayout(
                personalDataCard,
                serviceDataCard);

        details.setWidthFull();
        details.setPadding(false);
        details.setSpacing(true);
        details.setAlignItems(Alignment.STRETCH);

        details.getStyle()
                .set(
                        "box-sizing",
                        "border-box")
                .set(
                        "max-width",
                        CONTENT_MAX_WIDTH)
                .set(
                        "margin-inline",
                        "auto")
                .set(
                        "flex-wrap",
                        "wrap");

        return details;
    }

    private VerticalLayout createInformationCard(String title,
                                                 Component... rows) {
        H2 cardTitle = new H2(title);

        cardTitle.getStyle()
                .set(
                        "margin",
                        "0 0 var(--vaadin-gap-m)");

        VerticalLayout card = createCard();

        card.setSpacing(false);

        card.add(cardTitle);
        card.add(rows);

        return card;
    }

    private Div createInformationRow(String label,
                                     Span value) {
        Span labelComponent = new Span(label);

        labelComponent.getStyle()
                .set(
                        "display",
                        "block")
                .set(
                        "margin-bottom",
                        "var(--vaadin-gap-xs)")
                .set(
                        "color",
                        "var(--vaadin-text-color-secondary)")
                .set(
                        "font-size",
                        "13px");

        Div row = new Div();

        row.add(
                labelComponent,
                value);

        row.getStyle()
                .set(
                        "padding",
                        "var(--vaadin-padding-m) 0")
                .set(
                        "border-bottom",
                        "1px solid var(--vaadin-border-color-secondary)");

        return row;
    }

    private HorizontalLayout createSessionCard() {
        Span title = new Span("Текущая сессия");

        title.getStyle()
                .set(
                        "display",
                        "block")
                .set(
                        "margin-bottom",
                        "var(--vaadin-gap-xs)")
                .set(
                        "color",
                        "var(--aura-accent-text-color)")
                .set(
                        "font-weight",
                        "700");

        Span description = new Span("Вы авторизованы. После истечения срока действия потребуется повторный вход.");

        description.getStyle()
                .set(
                        "color",
                        "var(--vaadin-text-color-secondary)");

        Div descriptionContainer = new Div();

        descriptionContainer.add(
                title,
                description);

        descriptionContainer.getStyle()
                .set(
                        "flex",
                        "1 1 500px");

        sessionExpirationValue.setText("-");

        sessionExpirationValue.getStyle()
                .set(
                        "color",
                        "var(--aura-accent-text-color)")
                .set(
                        "font-weight",
                        "600")
                .set(
                        "white-space",
                        "nowrap");

        HorizontalLayout sessionCard = new HorizontalLayout(
                descriptionContainer,
                sessionExpirationValue);

        sessionCard.setWidthFull();
        sessionCard.setAlignItems(Alignment.CENTER);
        sessionCard.setJustifyContentMode(JustifyContentMode.BETWEEN);

        sessionCard.getStyle()
                .set(
                        "box-sizing",
                        "border-box")
                .set(
                        "max-width",
                        CONTENT_MAX_WIDTH)
                .set(
                        "margin-inline",
                        "auto")
                .set(
                        "flex-wrap",
                        "wrap")
                .set(
                        "gap",
                        "var(--vaadin-gap-m)")
                .set(
                        "padding",
                        "var(--vaadin-padding-l)")
                .set(
                        "border",
                        "1px solid var(--aura-accent-border-color)")
                .set(
                        "border-radius",
                        "var(--vaadin-radius-l)")
                .set(
                        "background",
                        "var(--aura-accent-surface)");

        return sessionCard;
    }

    private VerticalLayout createCard() {
        VerticalLayout card = new VerticalLayout();

        card.setWidthFull();
        card.setPadding(false);

        card.addClassName("aura-surface-solid");

        card.getStyle()
                .set(
                        "box-sizing",
                        "border-box")
                .set(
                        "padding",
                        "var(--vaadin-padding-l)")
                .set(
                        "border",
                        "1px solid var(--vaadin-border-color-secondary)")
                .set(
                        "border-radius",
                        "var(--vaadin-radius-l)")
                .set(
                        "box-shadow",
                        "0 8px 28px rgba(0, 0, 0, 0.18)");

        return card;
    }

    private Span createValueSpan() {
        Span value = new Span("-");

        value.getStyle()
                .set(
                        "display",
                        "block")
                .set(
                        "font-weight",
                        "600")
                .set(
                        "overflow-wrap",
                        "anywhere");

        return value;
    }

    private void loadProfile() {
        try {
            IndustrialSupportResponseData<UserResponse> response =
                    userClient.getCurrentUser(userSession.getAccessToken());

            UserResponse user = response.getData();

            if(user == null)
                throw new IllegalStateException("Support Service returned empty profile data.");

            displayProfile(user);
        } catch(RestClientResponseException ex) {
            showErrorNotification("Support Service отклонил запрос " + ex.getStatusCode());
        } catch(RestClientException ex) {
            showErrorNotification("Не удалось подключиться к Support Service");
        } catch(Exception ex) {
            showErrorNotification("Не удалось загрузить данные пользователя");
        }
    }

    private void displayProfile(UserResponse user) {
        avatarValue.setText(getInitials(user));
        fullNameValue.setText(getFullName(user));
        usernameValue.setText(formatUsername(user.username()));

        displayStatus(user.enabled());
        displayRoles(user);

        emailValue.setText(orDefault(
                user.email(),
                "Не указана"));

        organizationValue.setText(orDefault(
                user.organizationName(),
                "Не указана"));

        departmentValue.setText(orDefault(
                user.departmentName(),
                "Не указано"));

        telegramValue.setText(formatTelegramUsername(user.telegramUsername()));

        externalIdValue.setText(orDefault(
                user.externalId(),
                "Не указан"));

        createdAtValue.setText(formatDateTime(user.createdAt()));

        updatedAtValue.setText(formatDateTime(user.updatedAt()));

        sessionExpirationValue.setText(formatSessionExpiration(userSession.getExpiresAt()));
    }

    private void displayStatus(boolean enabled) {
        if(enabled) {
            statusValue.setText("Учетная запись активна");

            statusValue.getStyle()
                    .set(
                            "border-color",
                            "color-mix(in srgb, var(--aura-green) 45%, transparent)")
                    .set(
                            "background",
                            "color-mix(in srgb, var(--aura-green) 16%, transparent)")
                    .set(
                            "color",
                            "var(--aura-green)");
        } else {
            statusValue.setText("Учетная запись заблокирована");

            statusValue.getStyle()
                    .set(
                            "border-color",
                            "color-mix(in srgb, var(--aura-red) 45%, transparent)")
                    .set(
                            "background",
                            "color-mix(in srgb, var(--aura-red) 16%, transparent)")
                    .set(
                            "color",
                            "var(--aura-red)");
        }
    }

    private void displayRoles(UserResponse user) {
        rolesValue.removeAll();

        if(user.roles() == null || user.roles()
                .isEmpty()) {
            rolesValue.add(createRoleBadge("Роли не назначены"));

            return;
        }

        user.roles()
                .stream()
                .sorted(Comparator.comparing(RoleName::name))
                .map(this::getRoleLabel)
                .map(this::createRoleBadge)
                .forEach(rolesValue::add);
    }

    private Span createRoleBadge(String label) {
        Span badge = new Span(label);

        badge.getStyle()
                .set(
                        "padding",
                        "var(--vaadin-padding-xs) var(--vaadin-padding-m)")
                .set(
                        "border",
                        "1px solid var(--aura-accent-border-color)")
                .set(
                        "border-radius",
                        "999px")
                .set(
                        "background",
                        "var(--aura-accent-surface)")
                .set(
                        "color",
                        "var(--aura-accent-text-color)")
                .set(
                        "font-size",
                        "14px")
                .set(
                        "font-weight",
                        "600");

        return badge;
    }

    private String getFullName(UserResponse user) {
        String firstName = user.firstName() == null ? "" : user.firstName()
                .trim();

        String lastName = user.lastName() == null ? "" : user.lastName()
                .trim();

        String fullName = (firstName + " " + lastName).trim();

        if(!fullName.isEmpty())
            return fullName;

        return orDefault(
                user.username(),
                "Пользователь");
    }

    private String getInitials(UserResponse user) {
        StringBuilder initials = new StringBuilder();

        appendInitial(
                initials,
                user.firstName());

        appendInitial(
                initials,
                user.lastName());

        if(initials.isEmpty())
            appendInitial(
                    initials,
                    user.username());

        if(initials.isEmpty())
            return "?";

        return initials.toString()
                .toUpperCase(Locale.ROOT);
    }

    private void appendInitial(StringBuilder initials,
                               String value) {
        if(value == null || value.isBlank())
            return;

        initials.append(value.trim()
                .charAt(0));
    }

    private String formatUsername(String username) {
        if(username == null || username.isBlank())
            return "@не указан";

        String normalizedUsername = username.trim();

        if(normalizedUsername.startsWith("@"))
            return normalizedUsername;

        return "@" + normalizedUsername;
    }

    private String formatTelegramUsername(String telegramUsername) {
        if(telegramUsername == null || telegramUsername.isBlank())
            return "Не указан";

        String normalizedUsername = telegramUsername.trim();

        if(normalizedUsername.startsWith("@"))
            return normalizedUsername;

        return "@" + normalizedUsername;
    }

    private String formatDateTime(OffsetDateTime value) {
        if(value == null)
            return "Не указано";

        return DATE_TIME_FORMATTER.format(value);
    }

    private String formatSessionExpiration(Instant expiresAt) {
        if(expiresAt == null)
            return "Срок действия неизвестен";

        return "Действует до " + SESSION_DATE_TIME_FORMATTER.format(expiresAt);
    }

    private String orDefault(String value,
                             String defaultValue) {
        if(value == null || value.isBlank())
            return defaultValue;

        return value.trim();
    }

    private String getRoleLabel(RoleName role) {
        return switch(role) {
            case ROLE_EMPLOYEE -> "Сотрудник";
            case ROLE_DISPATCHER -> "Диспетчер";
            case ROLE_ENGINEER -> "Инженер";
            case ROLE_MANAGER -> "Менеджер";
            case ROLE_ADMIN -> "Администратор";
            case ROLE_ROBOT -> "Роботизированная система";
        };
    }

    private void showErrorNotification(String message) {
        Notification notification = Notification.show(
                message,
                5000,
                Notification.Position.TOP_END);

        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}
