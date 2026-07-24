package com.immx.industrialsupport.supportservice.services.outbox;

import com.immx.industrialsupport.supportservice.dto.outbox.AggregateType;
import com.immx.industrialsupport.supportservice.dto.outbox.OutboxEventType;
import com.immx.industrialsupport.supportservice.entities.OutboxEvent;
import com.immx.industrialsupport.supportservice.repositories.OutboxEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import java.util.UUID;

/**
 * Сервис отправки событий.
 */
@Service
public class OutboxService implements IOutboxService {

    @Autowired
    private OutboxEventRepository outboxEventRepository;

    @Autowired
    private JsonMapper jsonMapper;

    public OutboxEvent save(AggregateType aggregateType,
                            UUID aggregateId,
                            OutboxEventType eventType,
                            Object payload) {
        try {
            String serializedPayload = jsonMapper.writeValueAsString(payload);

            OutboxEvent event = new OutboxEvent(
                    aggregateType,
                    aggregateId,
                    eventType,
                    serializedPayload);

            return outboxEventRepository.save(event);
        } catch(JacksonException ex) {
            throw new IllegalStateException(
                    "Could not serialize outbox event payload",
                    ex);
        }
    }
}
