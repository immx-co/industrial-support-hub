package com.immx.industrialsupport.supportservice.entities;

import com.immx.industrialsupport.integrationcontracts.common.AggregateType;
import com.immx.industrialsupport.integrationcontracts.common.EventType;
import com.immx.industrialsupport.supportservice.dto.outbox.OutboxEventStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Модель события.
 */
@Entity
@Table(name = "outbox_events")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    @ToString.Include
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "aggregate_type",
            nullable = false,
            length = 50
    )
    @ToString.Include
    private AggregateType aggregateType;

    @Column(
            name = "aggregate_id",
            nullable = false
    )
    @ToString.Include
    private UUID aggregateId;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "event_type",
            nullable = false,
            length = 100
    )
    @ToString.Include
    private EventType eventType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(
            name = "payload",
            nullable = false,
            columnDefinition = "jsonb"
    )
    private String payload;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    @ToString.Include
    private OutboxEventStatus status;

    @Column(
            name = "retry_count",
            nullable = false
    )
    @ToString.Include
    private int retryCount;

    @CreationTimestamp
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    @ToString.Include
    private OffsetDateTime createdAt;

    @Column(name = "published_at")
    @ToString.Include
    private OffsetDateTime publishedAt;

    @Column(
            name = "last_error",
            columnDefinition = "text"
    )
    private String lastError;

    /**
     * ctor класса OutboxEvent.
     *
     * @param aggregateType тип сущности, породившей событие
     * @param aggregateId   идентификатор сущности
     * @param eventType     тип события
     * @param payload       данные события
     */
    public OutboxEvent(AggregateType aggregateType,
                       UUID aggregateId,
                       EventType eventType,
                       String payload) {
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.status = OutboxEventStatus.NEW;
        this.retryCount = 0;
    }

    /**
     * Помечает событие как опубликованное.
     */
    public void markAsPublished() {
        status = OutboxEventStatus.PUBLISHED;
        publishedAt = OffsetDateTime.now();
        lastError = null;
    }

    /**
     * Помечает событие как упавшее, не опубликованное.
     *
     * @param errorMessage строка ошибки при публикации
     */
    public void markAsFailed(String errorMessage) {
        status = OutboxEventStatus.FAILED;
        retryCount++;
        lastError = errorMessage;
    }

    /**
     * Помечает событие как упавшее, ориентируясь на количество попыток.
     *
     * @param errorMessage строка ошибки при публикации
     * @param maxRetries   максимальное количество попыток публикации
     */
    public void registerFailure(String errorMessage,
                                int maxRetries) {
        retryCount++;
        lastError = errorMessage;

        if(retryCount >= maxRetries)
            status = OutboxEventStatus.FAILED;
        else
            status = OutboxEventStatus.NEW;
    }

    /**
     * Подготавливает событие к повторной публикации.
     */
    public void prepareForRetry() {
        status = OutboxEventStatus.NEW;
    }
}
