package com.immx.industrialsupport.contracts.incident;

/**
 * Перечисление статусов обращений.
 */
public enum IncidentStatus {

    /**
     * Недавно созданное обращение.
     */
    NEW,

    /**
     * Обращение назначено на исполнение.
     */
    ASSIGNED,

    /**
     * Обращение взято в работу.
     */
    IN_PROGRESS,

    /**
     * Обращение решено.
     */
    RESOLVED,

    /**
     * Обращение закрыто.
     */
    CLOSED,

    /**
     * Обращение отменено.
     */
    CANCELLED,
}
