package com.immx.industrialsupport.supportservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/**
 * Конфигурация системного времени приложения.
 */
@Configuration
public class TimeConfiguration {

    /**
     * Предоставляет системные часы в UTC.
     *
     * @return системные часы
     */
    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
