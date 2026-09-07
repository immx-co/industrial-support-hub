package com.immx.industrialsupport.webui;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.aura.Aura;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Push
@StyleSheet(Aura.STYLESHEET)
@SpringBootApplication
public class SupportWebUiApplication implements AppShellConfigurator {
    public static void main(String[] args) {
        SpringApplication.run(
                SupportWebUiApplication.class,
                args);
    }
}
