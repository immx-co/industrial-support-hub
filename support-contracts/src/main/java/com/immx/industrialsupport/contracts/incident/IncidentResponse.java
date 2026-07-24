package com.immx.industrialsupport.contracts.incident;

import java.time.OffsetDateTime;
import java.util.UUID;

public record IncidentResponse(UUID id,
                               UUID organizationId,
                               UUID departmentId,
                               UUID reporterId,
                               UUID assignedEngineerId,
                               String title,
                               String description,
                               IncidentPriority priority,
                               IncidentStatus status,
                               OffsetDateTime slaDeadline,
                               OffsetDateTime resolvedAt,
                               OffsetDateTime closedAt,
                               OffsetDateTime createdAt,
                               OffsetDateTime updatedAt) {
}
