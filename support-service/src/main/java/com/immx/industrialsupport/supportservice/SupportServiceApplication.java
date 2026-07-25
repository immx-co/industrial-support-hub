package com.immx.industrialsupport.supportservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SupportServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                SupportServiceApplication.class,
                args);
    }
}
