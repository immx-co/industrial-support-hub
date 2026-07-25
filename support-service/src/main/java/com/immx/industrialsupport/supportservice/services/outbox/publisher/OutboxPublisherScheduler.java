package com.immx.industrialsupport.supportservice.services.outbox.publisher;

import com.immx.industrialsupport.supportservice.dto.outbox.OutboxEventStatus;
import com.immx.industrialsupport.supportservice.entities.OutboxEvent;
import com.immx.industrialsupport.supportservice.repositories.OutboxEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Планировщик публикации накопленных <code>Outbox</code> событий.
 */
@Component
@ConditionalOnProperty(
        name = "app.outbox.publisher.enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class OutboxPublisherScheduler {

    @Autowired
    private OutboxEventRepository outboxEventRepository;

    @Autowired
    private OutboxEventPublisher outboxEventPublisher;

    @Value("${app.outbox.publisher.batch-size:100}")
    private int batchSize;

    /**
     * Находит ожидающие публикации <code>Outbox</code> события и запускает их последовательную отправку в
     * <code>Kafka</code>.
     */
    @Scheduled(fixedDelayString = "${app.outbox.publisher.delay:5000}")
    public void publishPendingEvents() {
        List<UUID> eventIds = outboxEventRepository.findByStatusOrderByCreatedAtAsc(
                        OutboxEventStatus.NEW,
                        PageRequest.of(
                                0,
                                batchSize))
                .stream()
                .map(OutboxEvent::getId)
                .toList();

        for(UUID eventId : eventIds) {
            outboxEventPublisher.publish(eventId);

            if(Thread.currentThread()
                    .isInterrupted())
                break;
        }
    }
}
