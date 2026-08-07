package com.immx.industrialsupport.notificationworker.services.notification.consumer;

import com.immx.industrialsupport.integrationcontracts.common.EventEnvelope;
import com.immx.industrialsupport.notificationworker.services.notification.INotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

/**
 * Слушатель обращений <code>Kafka</code> уведомлений.
 */
@Slf4j
@Component
public class IncidentEventConsumer {

    @Autowired
    private JsonMapper jsonMapper;

    @Autowired
    private INotificationService notificationService;

    /**
     * Слушает <code>Kafka</code> <code>topic</code> и отправляет прочитанные <code>Kafka</code> сообщения на обработку.
     *
     * @param message прочитанное <code>Kafka</code> сообщение
     */
    @KafkaListener(topics = "${app.kafka.topics.incident-events}")
    public void consume(String message) {
        try {
            EventEnvelope<JsonNode> event = jsonMapper.readValue(
                    message,
                    new TypeReference<>() {
                    });

            log.info(
                    "Получено событие {} типа {}",
                    event.eventId(),
                    event.eventType());

            notificationService.process(event);
        } catch(Exception ex) {
            log.error(
                    "Ошибка обработки Kafka события: {}",
                    message,
                    ex);

            throw new IllegalStateException(
                    "Ошибка обработки Kafka события",
                    ex);
        }
    }
}
