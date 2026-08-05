package com.immx.industrialsupport.notificationworker.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Модель конфигурации телеграма.
 */
@Configuration
@EnableConfigurationProperties(TelegramProperties.class)
public class TelegramConfiguration {
}
