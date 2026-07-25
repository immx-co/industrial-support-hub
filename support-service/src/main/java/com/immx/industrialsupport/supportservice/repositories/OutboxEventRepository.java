package com.immx.industrialsupport.supportservice.repositories;

import com.immx.industrialsupport.supportservice.dto.outbox.OutboxEventStatus;
import com.immx.industrialsupport.supportservice.entities.OutboxEvent;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий отправки событий.
 */
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {

    List<OutboxEvent> findByStatusOrderByCreatedAtAsc(OutboxEventStatus status,
                                                      Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(
            """
                    SELECT event
                    FROM OutboxEvent event
                    WHERE event.id = :id
                    """
    )
    Optional<OutboxEvent> findByIdForUpdate(@Param("id") UUID id);
}
