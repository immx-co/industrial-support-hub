package com.immx.industrialsupport.webui.views;

import com.immx.industrialsupport.webui.session.UserSession;
import com.vaadin.flow.component.html.H1;
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

    public MainView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(new H1("Main Page"));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(!userSession.isAuthenticated())
            event.rerouteTo(LoginView.class);
    }
}
