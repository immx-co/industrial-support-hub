package com.immx.industrialsupport.supportservice.repositories;

import com.immx.industrialsupport.contracts.incident.IncidentStatus;
import com.immx.industrialsupport.supportservice.entities.Incident;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Интерфейс для работы с обращениями.
 */
public interface IncidentRepository extends JpaRepository<Incident, UUID> {

    @EntityGraph(
            attributePaths = { "organization", "department", "reporter", "assignedEngineer" }
    )
    List<Incident> findAllByOrganization_Id(UUID organizationId);

    @EntityGraph(
            attributePaths = { "organization", "department", "reporter", "assignedEngineer" }
    )
    Optional<Incident> findByIdAndOrganization_Id(UUID incidentId,
                                                  UUID organizationId);

    @EntityGraph(
            attributePaths = { "organization", "department", "reporter", "assignedEngineer" }
    )
    List<Incident> findAllByAssignedEngineer_Id(UUID engineerId);

    @Query(
            """
                    SELECT incident
                    FROM Incident incident
                    WHERE incident.slaDeadline < :now
                    AND incident.slaBreached = false
                    AND incident.status IN :statuses
                    ORDER BY incident.slaDeadline
                    """
    )
    List<Incident> findOverdueIncidents(@Param("now") OffsetDateTime now,
                                        @Param("statuses") Collection<IncidentStatus> statuses,
                                        Pageable pageable);
}
