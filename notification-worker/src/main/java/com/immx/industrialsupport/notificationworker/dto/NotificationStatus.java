package com.immx.industrialsupport.notificationworker.dto;

/**
 * Перечисление статусов уведомлений.
 */
public enum NotificationStatus {

    /**
     * Уведомление еще не отправлено.
     */
    PENDING,

    /**
     * Уведомление отправлено.
     */
    SENT,

    /**
     * Произошла ошибка отправки, уведомление не отправлено.
     */
    FAILED,
}
