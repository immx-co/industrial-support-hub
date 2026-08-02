package com.immx.industrialsupport.notificationworker.services.notification;

import com.immx.industrialsupport.contracts.role.RoleName;
import com.immx.industrialsupport.integrationcontracts.common.EventEnvelope;
import com.immx.industrialsupport.integrationcontracts.incident.IncidentAssignedEvent;
import com.immx.industrialsupport.integrationcontracts.incident.IncidentCreatedEvent;
import com.immx.industrialsupport.integrationcontracts.incident.IncidentSlaBreachedEvent;
import com.immx.industrialsupport.integrationcontracts.incident.IncidentStatusChangedEvent;
import com.immx.industrialsupport.notificationworker.dto.NotificationChannel;
import com.immx.industrialsupport.notificationworker.dto.NotificationRecipientType;
import com.immx.industrialsupport.notificationworker.entities.Notification;
import com.immx.industrialsupport.notificationworker.repositories.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.util.UUID;

/**
 * Сервис по отправке уведомлений о произошедших событиях, которые хранятся в <code>Kafka</code>.
 */
@Slf4j
@Service
public class NotificationService implements INotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private JsonMapper jsonMapper;

    /**
     * {@inheritDoc}
     */
    @Transactional
    public void process(EventEnvelope<JsonNode> event) {
        switch(event.eventType()) {
            case INCIDENT_CREATED -> processIncidentCreated(event);
            case INCIDENT_ASSIGNED -> processIncidentAssigned(event);
            case INCIDENT_STATUS_CHANGED -> processIncidentStatusChanged(event);
            case INCIDENT_SLA_BREACHED -> processIncidentSlaBreached(event);
        }
    }

    private void processIncidentCreated(EventEnvelope<JsonNode> envelope) {
        IncidentCreatedEvent event = convertPayload(
                envelope,
                IncidentCreatedEvent.class);

        createNotification(
                envelope,
                event.organizationId(),
                event.incidentId(),
                NotificationRecipientType.ROLE,
                RoleName.ROLE_DISPATCHER.name(),
                "Создано новое обращение",
                "Создано обращение " + event.incidentId() + " с приоритетом " + event.priority());
    }

    private void processIncidentAssigned(EventEnvelope<JsonNode> envelope) {
        IncidentAssignedEvent event = convertPayload(
                envelope,
                IncidentAssignedEvent.class);

        createNotification(
                envelope,
                event.organizationId(),
                event.incidentId(),
                NotificationRecipientType.USER,
                event.engineerId()
                        .toString(),
                "Назначено обращение",
                "На вас назначено обращение " + event.incidentId());
    }

    private void processIncidentStatusChanged(EventEnvelope<JsonNode> envelope) {
        IncidentStatusChangedEvent event = convertPayload(
                envelope,
                IncidentStatusChangedEvent.class);

        createNotification(
                envelope,
                event.organizationId(),
                event.incidentId(),
                NotificationRecipientType.USER,
                event.reporterId()
                        .toString(),
                "Статус обращения изменён",
                "Статус обращения " + event.incidentId() + " изменен с " + event.previousStatus() + " на "
                        + event.newStatus());
    }

    private void processIncidentSlaBreached(EventEnvelope<JsonNode> envelope) {
        IncidentSlaBreachedEvent event = convertPayload(
                envelope,
                IncidentSlaBreachedEvent.class);

        createNotification(
                envelope,
                event.organizationId(),
                event.incidentId(),
                NotificationRecipientType.ROLE,
                RoleName.ROLE_DISPATCHER.name(),
                "Нарушен SLA",
                "У обращения " + event.incidentId() + " нарушен срок SLA");

        createNotification(
                envelope,
                event.organizationId(),
                event.incidentId(),
                NotificationRecipientType.ROLE,
                RoleName.ROLE_MANAGER.name(),
                "Нарушен SLA",
                "У обращения " + event.incidentId() + " нарушен срок SLA");
    }

    private <T> T convertPayload(EventEnvelope<JsonNode> envelope,
                                 Class<T> payloadType) {
        try {
            return jsonMapper.treeToValue(
                    envelope.payload(),
                    payloadType);
        } catch(Exception ex) {
            throw new IllegalArgumentException(
                    "Не удалось дисериализовать payload события " + envelope.eventId(),
                    ex);
        }
    }

    private void createNotification(EventEnvelope<JsonNode> envelope,
                                    UUID organizationId,
                                    UUID incidentId,
                                    NotificationRecipientType recipientType,
                                    String recipientValue,
                                    String subject,
                                    String message) {
        boolean alreadyExists = notificationRepository.existsByEventIdAndRecipientTypeAndRecipientValueAndChannel(
                envelope.eventId(),
                recipientType,
                recipientValue,
                NotificationChannel.LOG);

        if(alreadyExists) {
            log.info(
                    "Событие {} для получателя {} уже обработано.",
                    envelope.eventId(),
                    recipientValue);
            return;
        }

        Notification notification = Notification.create(
                envelope.eventId(),
                envelope.eventType(),
                organizationId,
                incidentId,
                recipientType,
                recipientValue,
                subject,
                message);

        notificationRepository.save(notification);

        log.info(
                "Уведомление отправлено. Получатель: {}, тема: {}, сообщение: {}",
                recipientValue,
                subject,
                message);

        notification.markAsSent();
    }
}
