package com.immx.industrialsupport.notificationworker.entities;

import com.immx.industrialsupport.integrationcontracts.common.EventType;
import com.immx.industrialsupport.notificationworker.dto.NotificationChannel;
import com.immx.industrialsupport.notificationworker.dto.NotificationRecipientType;
import com.immx.industrialsupport.notificationworker.dto.NotificationStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(name = "notifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(
            name = "event_id",
            nullable = false
    )
    private UUID eventId;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "event_type",
            nullable = false
    )
    private EventType eventType;

    @Column(
            name = "organization_id",
            nullable = false
    )
    private UUID organizationId;

    @Column(
            name = "incident_id",
            nullable = false
    )
    private UUID incidentId;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "recipient_type",
            nullable = false
    )
    private NotificationRecipientType recipientType;

    @Column(
            name = "recipient_value",
            nullable = false
    )
    private String recipientValue;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "channel",
            nullable = false
    )
    private NotificationChannel channel;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false
    )
    private NotificationStatus status;

    @Column(
            name = "subject",
            nullable = false
    )
    private String subject;

    @Column(
            name = "message",
            nullable = false
    )
    private String message;

    @Column(
            name = "retry_count",
            nullable = false
    )
    private int retryCount;

    @Column(name = "last_error")
    private String lastError;

    @Column(
            name = "created_at",
            nullable = false
    )
    private OffsetDateTime createdAt;

    @Column(name = "sent_at")
    private OffsetDateTime sentAt;

    public static Notification create(UUID eventId,
                                      EventType eventType,
                                      UUID organizationId,
                                      UUID incidentId,
                                      NotificationRecipientType recipientType,
                                      String recipientValue,
                                      NotificationChannel channel,
                                      String subject,
                                      String message) {
        Notification notification = new Notification();

        notification.eventId = eventId;
        notification.eventType = eventType;
        notification.organizationId = organizationId;
        notification.incidentId = incidentId;
        notification.recipientType = recipientType;
        notification.recipientValue = recipientValue;
        notification.channel = channel;
        notification.status = NotificationStatus.PENDING;
        notification.subject = subject;
        notification.message = message;
        notification.retryCount = 0;
        notification.createdAt = OffsetDateTime.now();

        return notification;
    }

    /**
     * Помечает уведомление как отправленное.
     */
    public void markAsSent() {
        status = NotificationStatus.SENT;
        sentAt = OffsetDateTime.now();
        lastError = null;
    }

    /**
     * Помечает уведомление как завершившееся с ошибкой отправки.
     *
     * @param exception ошибка, вызвавшая отправку уведомления
     */
    public void registerFailure(Exception exception) {
        status = NotificationStatus.FAILED;
        retryCount++;
        lastError = exception.getMessage();
    }
}
