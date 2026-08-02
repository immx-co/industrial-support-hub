package com.immx.industrialsupport.notificationworker.repositories;

import com.immx.industrialsupport.notificationworker.dto.NotificationChannel;
import com.immx.industrialsupport.notificationworker.dto.NotificationRecipientType;
import com.immx.industrialsupport.notificationworker.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Репозиторий для работы с уведомлениями.
 */
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    boolean existsByEventIdAndRecipientTypeAndRecipientValueAndChannel(UUID eventId,
                                                                       NotificationRecipientType recipientType,
                                                                       String recipientValue,
                                                                       NotificationChannel channel);
}
