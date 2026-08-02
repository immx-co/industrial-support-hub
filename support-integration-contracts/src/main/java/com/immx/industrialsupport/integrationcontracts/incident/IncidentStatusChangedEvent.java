package com.immx.industrialsupport.integrationcontracts.incident;

import com.immx.industrialsupport.contracts.incident.IncidentStatus;

import java.util.UUID;

/**
 * Событие изменения статуса обращения.
 *
 * @param incidentId     идентификатор обращения
 * @param organizationId идентификатор организации
 * @param reporterId     идентификатор заявителя
 * @param previousStatus статус обращения до изменения
 * @param newStatus      статус обращения после изменения
 */
public record IncidentStatusChangedEvent(UUID incidentId,
                                         UUID organizationId,
                                         UUID reporterId,
                                         IncidentStatus previousStatus,
                                         IncidentStatus newStatus) {
}
