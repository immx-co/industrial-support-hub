package com.immx.industrialsupport.supportservice.services.outbox.publisher;

import com.immx.industrialsupport.integrationcontracts.common.EventEnvelope;
import com.immx.industrialsupport.supportservice.dto.outbox.OutboxEventStatus;
import com.immx.industrialsupport.supportservice.entities.OutboxEvent;
import com.immx.industrialsupport.supportservice.repositories.OutboxEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.util.UUID;

/**
 * Публикует события из таблицы <code>Outbox</code> в <code>Kafka</code>.
 */
@Slf4j
@Service
public class OutboxEventPublisher {

    @Autowired
    private OutboxEventRepository outboxEventRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private JsonMapper jsonMapper;

    @Value("${app.kafka.topics.incident-events}")
    private String incidentEventsTopic;

    @Value("${app.outbox.publisher.max-retries:5}")
    private int maxRetries;

    /**
     * Публикует указанное <code>Outbox</code> событие в <code>Kafka</code>.
     *
     * @param eventId идентификатор публикуемого <code>Outbox</code> события
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publish(UUID eventId) {
        OutboxEvent event = outboxEventRepository.findByIdForUpdate(eventId)
                .orElse(null);

        if(event == null || event.getStatus() != OutboxEventStatus.NEW)
            return;

        try {
            JsonNode payload = jsonMapper.readTree(event.getPayload());

            EventEnvelope<JsonNode> envelope = new EventEnvelope<>(
                    event.getId(),
                    event.getEventType(),
                    1,
                    event.getCreatedAt(),
                    event.getAggregateType(),
                    event.getAggregateId(),
                    payload);

            kafkaTemplate.send(
                            incidentEventsTopic,
                            event.getAggregateId()
                                    .toString(),
                            envelope)
                    .get();

            log.info(
                    "Событие с идентификатором {} успешно опубликовано в Kafka.",
                    event.getId());

            event.markAsPublished();
        } catch(InterruptedException ex) {
            Thread.currentThread()
                    .interrupt();

            event.registerFailure(
                    getErrorMessage(ex),
                    maxRetries);
        } catch(Exception ex) {
            event.registerFailure(
                    getErrorMessage(ex),
                    maxRetries);
        }
    }

    private String getErrorMessage(Exception ex) {
        String message = ex.getMessage();

        if(message == null || message.isBlank())
            return ex.getClass()
                    .getSimpleName();

        return message;
    }
}
