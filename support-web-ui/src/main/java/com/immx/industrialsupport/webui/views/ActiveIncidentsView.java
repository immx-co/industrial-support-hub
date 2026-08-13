package com.immx.industrialsupport.webui.views;

import com.immx.industrialsupport.webui.layouts.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(
        value = "incidents",
        layout = MainLayout.class
)
@PageTitle("Активные обращения")
public class ActiveIncidentsView extends VerticalLayout {

    public ActiveIncidentsView() {
        setPadding(true);

        add(
                new H1("Активные обращения"),
                new Paragraph("Здесь будет список активных обращений"));
    }
}
