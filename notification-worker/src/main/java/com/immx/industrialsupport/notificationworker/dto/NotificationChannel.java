package com.immx.industrialsupport.notificationworker.dto;

/**
 * Перечисление каналов отправки уведомлений.
 */
public enum NotificationChannel {

    /**
     * Уведомление в логах приложения.
     */
    LOG,

    /**
     * Уведомление по электронной почте.
     */
    EMAIL,

    /**
     * Уведомление в телеграме.
     */
    TELEGRAM,

    /**
     * Уведомление вконтакте.
     */
    VK,
}
