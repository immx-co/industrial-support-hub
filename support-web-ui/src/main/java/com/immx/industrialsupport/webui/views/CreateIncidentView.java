package com.immx.industrialsupport.webui.views;

import com.immx.industrialsupport.client.IncidentClient;
import com.immx.industrialsupport.contracts.common.IndustrialSupportResponseData;
import com.immx.industrialsupport.contracts.incident.CreateIncidentRequest;
import com.immx.industrialsupport.contracts.incident.IncidentPriority;
import com.immx.industrialsupport.contracts.incident.IncidentResponse;
import com.immx.industrialsupport.webui.layouts.MainLayout;
import com.immx.industrialsupport.webui.session.UserSession;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

@Route(
        value = "incidents/new",
        layout = MainLayout.class
)
@PageTitle("Создать обращение")
public class CreateIncidentView extends VerticalLayout implements BeforeEnterObserver {

    @Autowired
    private IncidentClient incidentClient;

    @Autowired
    private UserSession userSession;

    private final TextField titleField = new TextField("Заголовок");

    private final TextArea descriptionField = new TextArea("Описание");

    private final Select<IncidentPriority> prioritySelect = new Select<>();

    private final Button clearButton = new Button("Очистить");

    private final Button createButton = new Button("Создать обращение");

    private final Span currentUserValue = new Span();

    private final Span departmentValue = new Span();

    public CreateIncidentView() {
        configurePage();
        configureFields();
        configureButtons();

        add(
                createHeader(),
                createContent());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(!userSession.isAuthenticated()) {
            event.forwardTo(LoginView.class);
            return;
        }

        currentUserValue.setText("@" + userSession.getUsername());

        departmentValue.setText(userSession.getDepartmentId()
                .toString());
    }

    private void configurePage() {
        setWidthFull();
        setPadding(true);
        setSpacing(true);
        setAlignItems(Alignment.CENTER);
    }

    private void configureFields() {
        titleField.setRequired(true);
        titleField.setRequiredIndicatorVisible(true);
        titleField.setMaxLength(200);
        titleField.setClearButtonVisible(true);
        titleField.setWidthFull();
        titleField.setPlaceholder("Например: не запускается CAD-система");
        titleField.setHelperText("Кратко сформулируйте суть проблемы");

        descriptionField.setRequired(true);
        descriptionField.setRequiredIndicatorVisible(true);
        descriptionField.setMinLength(10);
        descriptionField.setWidthFull();
        descriptionField.setMinHeight("220px");
        descriptionField.setPlaceholder("Опишите последовательность действий, текст ошибки и ожидаемый результат");

        prioritySelect.setLabel("Приоритет");
        prioritySelect.setRequiredIndicatorVisible(true);
        prioritySelect.setItems(IncidentPriority.values());
        prioritySelect.setItemLabelGenerator(this::getPriorityLabel);
        prioritySelect.setValue(IncidentPriority.MEDIUM);
        prioritySelect.setWidthFull();
    }

    private void configureButtons() {
        createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        clearButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        createButton.addClickListener(event -> createIncident());

        clearButton.addClickListener(event -> clearForm());
    }

    private Div createHeader() {
        Div header = new Div();

        H1 title = new H1("Создать обращение");
        Paragraph subtitle = new Paragraph("Сообщите технической поддержке о возникшей проблеме");

        title.getStyle()
                .setMargin("0");

        subtitle.getStyle()
                .setMargin("var(--lumo-space-xs) 0 0")
                .setColor("var(--lumo-secondary-text-color)");

        header.add(
                title,
                subtitle);

        header.setWidthFull();
        header.getStyle()
                .setMaxWidth("1100px");

        return header;
    }

    private FormLayout createContent() {
        FormLayout content = new FormLayout();

        content.setWidthFull();
        content.getStyle()
                .setMaxWidth("1100px");

        content.setResponsiveSteps(
                new FormLayout.ResponsiveStep(
                        "0",
                        1),
                new FormLayout.ResponsiveStep(
                        "900px",
                        3));

        VerticalLayout formCard = createFormCard();
        VerticalLayout informationCard = createInformationCard();

        content.add(
                formCard,
                informationCard);

        content.setColspan(
                formCard,
                2);

        return content;
    }

    private VerticalLayout createFormCard() {
        VerticalLayout formCard = createCard();

        HorizontalLayout actions = new HorizontalLayout(
                clearButton,
                createButton);

        actions.setWidthFull();
        actions.setJustifyContentMode(JustifyContentMode.END);

        formCard.add(
                titleField,
                descriptionField,
                prioritySelect,
                actions);

        return formCard;
    }

    private VerticalLayout createInformationCard() {
        VerticalLayout informationCard = createCard();

        H2 recommendationsTitle = new H2("Рекомендации");

        recommendationsTitle.getStyle()
                .setMargin("0");

        UnorderedList recommendations = new UnorderedList(
                new ListItem("Кратко опишите проблему"),
                new ListItem("Укажите название программы или оборудования"),
                new ListItem("Приведите точный текст ошибки"),
                new ListItem("Опишите действия, после которых возникла проблема"));

        H2 userTitle = new H2("Данные заявителя");

        userTitle.getStyle()
                .setMarginBottom("0");

        informationCard.add(
                recommendationsTitle,
                recommendations,
                userTitle,
                createInformationRow(
                        "Пользователь",
                        currentUserValue),
                createInformationRow(
                        "Подразделение",
                        departmentValue));

        return informationCard;
    }

    private VerticalLayout createCard() {
        VerticalLayout card = new VerticalLayout();

        card.setWidthFull();
        card.setPadding(true);
        card.setSpacing(true);

        card.getStyle()
                .setBorder("1px solid var(--lumo-contrast-10pct)")
                .setBorderRadius("var(--lumo-border-radius-l)")
                .setBackground("var(--lumo-base-color)")
                .setBoxShadow("var(--lumo-box-shadow-xs)");

        return card;
    }

    private Div createInformationRow(String label,
                                     Span value) {
        Div row = new Div();

        Span labelComponent = new Span(label);

        labelComponent.getStyle()
                .setDisplay(Style.Display.BLOCK)
                .setFontSize("var(--lumo-font-size-s)")
                .setColor("var(--lumo-secondary-text-color)");

        value.getStyle()
                .setDisplay(Style.Display.BLOCK)
                .setFontWeight("600")
                .setOverflow(Style.Overflow.AUTO);

        row.add(
                labelComponent,
                value);

        return row;
    }

    private void createIncident() {
        if(!validateForm())
            return;

        createButton.setEnabled(false);
        clearButton.setEnabled(false);

        try {
            CreateIncidentRequest request = new CreateIncidentRequest(
                    userSession.getDepartmentId(),
                    userSession.getUserId(),
                    titleField.getValue()
                            .trim(),
                    descriptionField.getValue()
                            .trim(),
                    prioritySelect.getValue());

            IndustrialSupportResponseData<IncidentResponse> response = incidentClient.createIncident(
                    userSession.getOrganizationId(),
                    request,
                    userSession.getAccessToken());

            showSuccessNotification(response.getMessage());

            clearForm();

            UI.getCurrent()
                    .navigate(ActiveIncidentsView.class);
        } catch(RestClientResponseException ex) {
            showErrorNotification("Support Service отклонил запрос: " + ex.getStatusCode());
        } catch(RestClientException ex) {
            showErrorNotification("Не удалось подключиться к Support Service");
        } catch(Exception ex) {
            showErrorNotification("Не удалось создать обращение");
        } finally {
            createButton.setEnabled(true);
            clearButton.setEnabled(true);
        }
    }

    private boolean validateForm() {
        boolean titleValid = titleField.getValue() != null && !titleField.getValue()
                .isBlank();

        boolean descriptionValid = descriptionField.getValue() != null && !descriptionField.getValue()
                .isBlank();

        boolean priorityValid = prioritySelect.getValue() != null;

        titleField.setInvalid(!titleValid);
        titleField.setErrorMessage("Введите заголовок обращения");

        descriptionField.setInvalid(!descriptionValid);
        descriptionField.setErrorMessage("Описание не должно быть пустым");

        prioritySelect.setInvalid(!priorityValid);
        prioritySelect.setErrorMessage("Выберите приоритет");

        if(titleValid && descriptionValid && priorityValid)
            return true;

        showErrorNotification("Проверьте заполнение формы");

        return false;
    }

    private void clearForm() {
        titleField.clear();
        descriptionField.clear();
        prioritySelect.setValue(IncidentPriority.LOW);

        titleField.setInvalid(false);
        descriptionField.setInvalid(false);
        prioritySelect.setInvalid(false);

        titleField.focus();
    }

    private String getPriorityLabel(IncidentPriority priority) {
        return switch(priority) {
            case LOW -> "Низкий";
            case MEDIUM -> "Средний";
            case HIGH -> "Высокий";
            case CRITICAL -> "Критический";
        };
    }

    private void showSuccessNotification(String message) {
        Notification notification = Notification.show(
                message,
                4000,
                Notification.Position.TOP_END);

        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private void showErrorNotification(String message) {
        Notification notification = Notification.show(
                message,
                5000,
                Notification.Position.TOP_END);

        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}
