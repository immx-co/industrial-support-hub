package com.immx.industrialsupport.webui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SupportWebUiApplication {
    public static void main(String[] args) {
        SpringApplication.run(
                SupportWebUiApplication.class,
                args);
    }
}
