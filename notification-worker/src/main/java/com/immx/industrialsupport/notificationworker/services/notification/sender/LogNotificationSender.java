package com.immx.industrialsupport.notificationworker.services.notification.sender;

import com.immx.industrialsupport.notificationworker.dto.NotificationChannel;
import com.immx.industrialsupport.notificationworker.entities.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Отправитель уведомления в лог.
 */
@Slf4j
@Component
public class LogNotificationSender implements INotificationSender {

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.LOG;
    }

    @Override
    public void send(Notification notification) {
        log.info(
                "Уведомление отправлено. Тип получателя: {}, получатель: {}, тема: {}, сообщение: {}",
                notification.getRecipientType(),
                notification.getRecipientValue(),
                notification.getSubject(),
                notification.getMessage());
    }
}
