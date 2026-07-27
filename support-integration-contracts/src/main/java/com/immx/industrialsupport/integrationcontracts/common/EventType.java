package com.immx.industrialsupport.integrationcontracts.common;

/**
 * Типы интеграционных событий.
 */
public enum EventType {

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

    /**
     * Срок <code>SLA</code> обращения истек.
     */
    INCIDENT_SLA_BREACHED,
}
