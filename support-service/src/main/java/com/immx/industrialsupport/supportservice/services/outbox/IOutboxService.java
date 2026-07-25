package com.immx.industrialsupport.supportservice.services.outbox;

import com.immx.industrialsupport.integrationcontracts.common.AggregateType;
import com.immx.industrialsupport.integrationcontracts.common.EventType;
import com.immx.industrialsupport.supportservice.entities.OutboxEvent;

import java.util.UUID;

/**
 * Интерфейс сервиса отправки событий.
 */
public interface IOutboxService {

    /**
     * Сохраняет событие.
     *
     * @param aggregateType тип сущности, которое породило событие
     * @param aggregateId   идентификатор конкретной сущности
     * @param eventType     тип события
     * @param payload       данные события
     * @return сохраненное событие
     */
    OutboxEvent save(AggregateType aggregateType,
                     UUID aggregateId,
                     EventType eventType,
                     Object payload);
}
