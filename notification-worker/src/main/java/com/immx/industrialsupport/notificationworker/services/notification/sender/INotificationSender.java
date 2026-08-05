package com.immx.industrialsupport.notificationworker.services.notification.sender;

import com.immx.industrialsupport.notificationworker.dto.NotificationChannel;
import com.immx.industrialsupport.notificationworker.entities.Notification;

/**
 * Интерфейс отправителя уведомлений.
 */
public interface INotificationSender {

    /**
     * Возвращает канал связи с пользователем.
     *
     * @return канал связи с пользователем.
     */
    NotificationChannel getChannel();

    /**
     * Отправляет уведомление пользователю.
     *
     * @param notification модель уведомления
     */
    void send(Notification notification);
}
