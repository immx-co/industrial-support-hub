package com.immx.industrialsupport.notificationworker.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Модель настроек телеграма.
 *
 * @param botToken   токен бота
 * @param testChatId идентификатор чата
 */
@ConfigurationProperties(prefix = "app.telegram")
public record TelegramProperties(String botToken,
                                 Long testChatId) {
}
