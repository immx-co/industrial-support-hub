package com.immx.industrialsupport.integrationcontracts.incident;

import com.immx.industrialsupport.contracts.incident.IncidentPriority;
import com.immx.industrialsupport.contracts.incident.IncidentStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Модель события создания нового обращения.
 *
 * @param incidentId     идентификатор созданного обращения
 * @param organizationId идентификатор организации
 * @param departmentId   идентификатор подразделения, из которого поступило обращение
 * @param reporterId     идентификатор пользователя, создавшего обращение
 * @param priority       приоритет обращения
 * @param status         статус обращения
 * @param createdAt      дата и время создания обращения
 */
public record IncidentCreatedEvent(UUID incidentId,
                                   UUID organizationId,
                                   UUID departmentId,
                                   UUID reporterId,
                                   IncidentPriority priority,
                                   IncidentStatus status,
                                   OffsetDateTime createdAt) {
}
