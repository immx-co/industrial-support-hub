package com.immx.industrialsupport.notificationworker.dto;

/**
 * Перечисление типов получателей уведомления.
 */
public enum NotificationRecipientType {

    /**
     * Конкретному пользователю.
     */
    USER,

    /**
     * Всем с указанной ролью.
     */
    ROLE,
}
