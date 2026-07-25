package com.immx.industrialsupport.integrationcontracts.incident;

import com.immx.industrialsupport.contracts.incident.IncidentStatus;

import java.util.UUID;

/**
 * Событие назначения инженера на обращение.
 *
 * @param incidentId     идентификатор обращения
 * @param organizationId идентификатор организации
 * @param engineerId     идентификатор назначенного инженера
 * @param status         статус обращения после назначения
 */
public record IncidentAssignedEvent(UUID incidentId,
                                    UUID organizationId,
                                    UUID engineerId,
                                    IncidentStatus status) {
}
