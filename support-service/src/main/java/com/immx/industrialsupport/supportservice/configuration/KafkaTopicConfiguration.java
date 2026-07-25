package com.immx.industrialsupport.supportservice.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Конфигурация <code>Kafka</code>.
 * Регистрирует необходимые <code>Kafka</code> топики при запуске приложения,
 * если интеграция с Kafka включена настройкой {@code app.kafka.enabled}.
 */
@Configuration
@ConditionalOnProperty(
        name = "app.kafka.enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class KafkaTopicConfiguration {

    /**
     * Создаёт описание <code>Kafka</code> топика для публикации событий обращений.
     *
     * @param topicName название топика из конфигурации приложения
     * @return конфигурация <code>Kafka</code> топика событий обращений
     */
    @Bean
    public NewTopic incidentEventsTopic(@Value("${app.kafka.topics.incident-events}") String topicName) {
        return TopicBuilder.name(topicName)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
