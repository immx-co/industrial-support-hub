package com.immx.industrialsupport.notificationworker.services.notification;

import com.immx.industrialsupport.integrationcontracts.common.EventEnvelope;
import tools.jackson.databind.JsonNode;

/**
 * Интерфейс сервиса по отправке уведомлений.
 */
public interface INotificationService {

    /**
     * Обрабатывает <code>Kafka</code> событие и производит отправку уведомления о произошедшем событии.
     *
     * @param event произошедшее событие, хранящееся в <code>Kafka</code>
     */
    void process(EventEnvelope<JsonNode> event);
}
