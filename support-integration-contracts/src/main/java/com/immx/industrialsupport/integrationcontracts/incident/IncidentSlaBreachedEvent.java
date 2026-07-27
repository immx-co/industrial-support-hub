package com.immx.industrialsupport.integrationcontracts.incident;

import com.immx.industrialsupport.contracts.incident.IncidentPriority;
import com.immx.industrialsupport.contracts.incident.IncidentStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Модель истекшего SLA обращения.
 *
 * @param incidentId     идентификатор обращения, которое истекло
 * @param organizationId идентификатор организации
 * @param departmentId   идентификатор подразделения
 * @param priority       приоритет обращения
 * @param status         статус обращения
 * @param slaDeadline    дата и время, когда истечет срок <code>SLA</code> обращения
 * @param breachedAt     дата и время, когда зафиксирован срок истечения <code>SLA</code> обращения
 */
public record IncidentSlaBreachedEvent(UUID incidentId,
                                       UUID organizationId,
                                       UUID departmentId,
                                       IncidentPriority priority,
                                       IncidentStatus status,
                                       OffsetDateTime slaDeadline,
                                       OffsetDateTime breachedAt) {
}
