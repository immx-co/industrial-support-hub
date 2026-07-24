package com.immx.industrialsupport.supportservice.repositories;

import com.immx.industrialsupport.supportservice.dto.outbox.OutboxEventStatus;
import com.immx.industrialsupport.supportservice.entities.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий отправки событий.
 */
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {

    List<OutboxEvent> findTop100ByStatusOrderByCreatedAtAsc(OutboxEventStatus status);
}
