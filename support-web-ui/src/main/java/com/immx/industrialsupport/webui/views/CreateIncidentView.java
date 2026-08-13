package com.immx.industrialsupport.webui.views;

import com.immx.industrialsupport.webui.layouts.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(
        value = "incidents/new",
        layout = MainLayout.class
)
@PageTitle("Создать обращение")
public class CreateIncidentView extends VerticalLayout {

    public CreateIncidentView() {
        setPadding(true);

        add(
                new H1("Создать обращение"),
                new Paragraph("Здесь будет форма создания обращения"));
    }
}
