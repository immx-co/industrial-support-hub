package com.immx.industrialsupport.supportservice.services.outbox;

import com.immx.industrialsupport.integrationcontracts.common.AggregateType;
import com.immx.industrialsupport.integrationcontracts.common.EventType;
import com.immx.industrialsupport.supportservice.entities.OutboxEvent;
import com.immx.industrialsupport.supportservice.repositories.OutboxEventRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import java.util.UUID;

/**
 * Сервис отправки событий.
 */
@Slf4j
@Service
public class OutboxService implements IOutboxService {

    @Autowired
    private OutboxEventRepository outboxEventRepository;

    @Autowired
    private JsonMapper jsonMapper;

    @Transactional(Transactional.TxType.MANDATORY)
    public OutboxEvent save(AggregateType aggregateType,
                            UUID aggregateId,
                            EventType eventType,
                            Object payload) {
        try {
            String serializedPayload = jsonMapper.writeValueAsString(payload);

            OutboxEvent event = new OutboxEvent(
                    aggregateType,
                    aggregateId,
                    eventType,
                    serializedPayload);

            OutboxEvent savedOutboxEvent = outboxEventRepository.save(event);

            log.info(
                    "Событие с идентификатором {} успешно сохранено.",
                    savedOutboxEvent.getId());

            return savedOutboxEvent;
        } catch(JacksonException ex) {
            throw new IllegalStateException(
                    "Could not serialize outbox event payload",
                    ex);
        }
    }
}
