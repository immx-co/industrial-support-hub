package com.immx.industrialsupport.integrationcontracts.common;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Оболочка интеграционного события, публикуемого в <code>Kafka</code>.
 *
 * @param eventId       идентификатор события
 * @param eventType     тип произошедшего события
 * @param eventVersion  версия структуры события
 * @param occurredAt    дата и время возникновения события
 * @param aggregateType тип сущности, породившей событие
 * @param aggregateId   идентификатор конкретной сущности, породившей событие
 * @param payload       полезная нагрузка события
 * @param <T>           тип полезной нагрузки события
 */
public record EventEnvelope<T>(UUID eventId,
                               EventType eventType,
                               int eventVersion,
                               OffsetDateTime occurredAt,
                               AggregateType aggregateType,
                               UUID aggregateId,
                               T payload) {
}
