package com.immx.industrialsupport.webui.views;

import com.immx.industrialsupport.client.IncidentClient;
import com.immx.industrialsupport.contracts.incident.IncidentPriority;
import com.immx.industrialsupport.webui.layouts.MainLayout;
import com.immx.industrialsupport.webui.session.UserSession;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

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
        setPadding(true);

        add(
                new H1("Создать обращение"),
                new Paragraph("Здесь будет форма создания обращения"));
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
}
