package com.immx.industrialsupport.webui.views;

import com.immx.industrialsupport.client.IncidentClient;
import com.immx.industrialsupport.contracts.common.IndustrialSupportResponseData;
import com.immx.industrialsupport.contracts.incident.IncidentPriority;
import com.immx.industrialsupport.contracts.incident.IncidentResponse;
import com.immx.industrialsupport.contracts.incident.IncidentStatus;
import com.immx.industrialsupport.contracts.role.RoleName;
import com.immx.industrialsupport.webui.layouts.MainLayout;
import com.immx.industrialsupport.webui.session.UserSession;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Route(
        value = "incidents",
        layout = MainLayout.class
)
@PageTitle("Активные обращения")
public class ActiveIncidentsView extends VerticalLayout implements BeforeEnterObserver {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Autowired
    private IncidentClient incidentClient;

    @Autowired
    private UserSession userSession;

    private final Grid<IncidentResponse> incidentsGrid = new Grid<>(
            IncidentResponse.class,
            false);

    private final Span incidentsCount = new Span();

    private final Paragraph scopeDescription = new Paragraph();

    private final Button refreshButton = new Button("Обновить");

    public ActiveIncidentsView() {
        configurePage();
        configureGrid();
        configureRefreshButton();

        VerticalLayout header = createHeader();

        add(
                header,
                incidentsGrid);

        expand(incidentsGrid);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(!userSession.isAuthenticated()) {
            event.forwardTo(LoginView.class);
            return;
        }

        scopeDescription.setText(getScopeDescription());

        loadIncidents();
    }

    private void configurePage() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);
    }

    private void configureGrid() {
        incidentsGrid.setSizeFull();

        incidentsGrid.addThemeVariants(
                GridVariant.LUMO_ROW_STRIPES,
                GridVariant.LUMO_COLUMN_BORDERS);

        incidentsGrid.addComponentColumn(incident -> createStatusBadge(incident.status()))
                .setHeader("Статус")
                .setSortable(true)
                .setResizable(true);

        incidentsGrid.addComponentColumn(incident -> createPriorityBadge(incident.priority()))
                .setHeader("Приоритет")
                .setSortable(true)
                .setResizable(true);

        incidentsGrid.addColumn(incident -> formatDateTime(incident.createdAt()))
                .setHeader("Создано")
                .setSortable(true)
                .setResizable(true);

        incidentsGrid.addColumn(incident -> formatDateTime(incident.createdAt()))
                .setHeader("Срок SLA")
                .setSortable(true)
                .setResizable(true);

        incidentsGrid.addComponentColumn(this::createSlaBadge)
                .setHeader("SLA")
                .setResizable(true);
    }

    private void configureRefreshButton() {
        refreshButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        refreshButton.addClickListener(event -> loadIncidents());
    }

    private VerticalLayout createHeader() {
        H1 title = new H1("Активные обращения");

        title.getStyle()
                .setMargin("0");

        scopeDescription.getStyle()
                .setMargin("0")
                .setColor("var(--lumo-secondary-text-color)");

        incidentsCount.getStyle()
                .setFontWeight("600");

        HorizontalLayout actions = new HorizontalLayout(
                incidentsCount,
                refreshButton);

        actions.setAlignItems(Alignment.CENTER);

        HorizontalLayout headerRow = new HorizontalLayout(
                title,
                actions);

        headerRow.setWidthFull();
        headerRow.setAlignItems(Alignment.CENTER);
        headerRow.setJustifyContentMode(JustifyContentMode.BETWEEN);

        VerticalLayout header = new VerticalLayout(
                headerRow,
                scopeDescription);

        header.setWidthFull();
        header.setPadding(true);
        header.setSpacing(true);

        return header;
    }

    private void loadIncidents() {
        refreshButton.setEnabled(false);

        try {
            IndustrialSupportResponseData<List<IncidentResponse>> response =
                    incidentClient.getActiveIncidents(userSession.getAccessToken());

            List<IncidentResponse> incidents = response.getData() == null ? List.of() : response.getData();

            incidentsGrid.setItems(incidents);

            incidentsCount.setText("Найдено " + incidents.size());
        } catch(RestClientResponseException ex) {
            incidentsGrid.setItems(List.of());

            showErrorNotification("Support Service отклонил запрос: " + ex.getStatusCode());
        } catch(RestClientException ex) {
            incidentsGrid.setItems(List.of());

            showErrorNotification("Не удалось подключиться к Support Service");
        } finally {
            refreshButton.setEnabled(true);
        }
    }

    private Span createStatusBadge(IncidentStatus status) {
        Span badge = new Span(getStatusLabel(status));

        badge.getElement()
                .getThemeList()
                .add("badge " + getStatusTheme(status));

        return badge;
    }

    private Span createPriorityBadge(IncidentPriority priority) {
        Span badge = new Span(getPriorityLabel(priority));

        badge.getElement()
                .getThemeList()
                .add("badge " + getPriorityTheme(priority));

        return badge;
    }

    private Span createSlaBadge(IncidentResponse incident) {
        Span badge;

        if(incident.slaBreached()) {
            badge = new Span("Нарушен");

            badge.getElement()
                    .getThemeList()
                    .add("badge error");
        } else {
            badge = new Span("В срок");

            badge.getElement()
                    .getThemeList()
                    .add("badge success");
        }

        return badge;
    }

    private String getScopeDescription() {
        if(userSession.hasRole(RoleName.ROLE_ADMIN) || userSession.hasRole(RoleName.ROLE_MANAGER)
           || userSession.hasRole(RoleName.ROLE_DISPATCHER))
            return "Показаны активные обращения всей организации";

        if(userSession.getRoles()
                   .size() > 1)
            return "Обращения показаны с учетом всех ваших ролей";

        if(userSession.hasRole(RoleName.ROLE_ENGINEER))
            return "Показаны обращения, назначенные на вас";

        return "Показаны созданные вами активные обращения";
    }

    private String getStatusLabel(IncidentStatus status) {
        return switch(status) {
            case NEW -> "Новое";
            case ASSIGNED -> "Назначено";
            case IN_PROGRESS -> "В работе";
            case RESOLVED -> "Решено";
            case CLOSED -> "Закрыто";
            case CANCELLED -> "Отменено";
        };
    }

    private String getStatusTheme(IncidentStatus status) {
        return switch(status) {
            case NEW -> "contrast";
            case ASSIGNED -> "primary";
            case IN_PROGRESS -> "warning";
            case RESOLVED, CLOSED -> "success";
            case CANCELLED -> "error";
        };
    }

    private String getPriorityLabel(IncidentPriority priority) {
        return switch(priority) {
            case LOW -> "Низкий";
            case MEDIUM -> "Средний";
            case HIGH -> "Высокий";
            case CRITICAL -> "Критический";
        };
    }

    private String getPriorityTheme(IncidentPriority priority) {
        return switch(priority) {
            case LOW -> "contrast";
            case MEDIUM -> "primary";
            case HIGH -> "warning";
            case CRITICAL -> "error";
        };
    }

    private String formatDateTime(OffsetDateTime value) {
        if(value == null)
            return "-";

        return DATE_TIME_FORMATTER.format(value);
    }

    private void showErrorNotification(String message) {
        Notification notification = Notification.show(
                message,
                5000,
                Notification.Position.TOP_END);

        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}
