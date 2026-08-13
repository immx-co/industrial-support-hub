package com.immx.industrialsupport.webui.views;

import com.immx.industrialsupport.webui.layouts.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(
        value = "profile",
        layout = MainLayout.class
)
@PageTitle("Личный кабинет")
public class ProfileView extends VerticalLayout {

    public ProfileView() {
        setPadding(true);

        add(
                new H1("Личный кабинет"),
                new Paragraph("Здесь будут данные пользователя"));
    }
}
