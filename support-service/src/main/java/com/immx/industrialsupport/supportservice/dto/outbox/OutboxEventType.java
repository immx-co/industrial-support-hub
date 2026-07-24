package com.immx.industrialsupport.supportservice.dto.outbox;

/**
 * Перечисление описаний типов событий.
 */
public enum OutboxEventType {

    /**
     * Обращение создано.
     */
    INCIDENT_CREATED,

    /**
     * Обращение назначено на выполнение объекту.
     */
    INCIDENT_ASSIGNED,

    /**
     * Статус обращения изменен.
     */
    INCIDENT_STATUS_CHANGED,
}
