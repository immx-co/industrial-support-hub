package com.immx.industrialsupport.supportservice.services.sla;

/**
 * Интерфейс сервиса по отслеживанию истекших обращений.
 */
public interface ISlaMonitoringService {

    /**
     * Находит просроченные обращения, фиксирует нарушение <code>SLA</code> и создаёт интеграционные события в
     * <code>Outbox</code>.
     *
     * @return количество обработанный обращений
     */
    int processOverdueIncidents();
}
