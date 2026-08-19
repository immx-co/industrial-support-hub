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


    @EntityGraph(attributePaths = { "organization", "department", "reporter", "assignedEngineer" })
    @Query(
            """
                    SELECT incident
                    FROM Incident incident
                    WHERE incident.organization.id = :organizationId
                    AND incident.status IN (:statuses)
                    ORDER BY incident.createdAt DESC
                    """
    )
    List<Incident> findActiveByOrganization(@Param("organizationId") UUID organizationId,
                                            @Param("statuses") Collection<IncidentStatus> statuses);

    @Query(
            """
                    SELECT incident
                    FROM Incident incident
                    WHERE incident.organization.id = :organizationId
                    AND incident.reporter.id = :reporterId
                    AND incident.status IN (:statuses)
                    ORDER BY incident.createdAt DESC
                    """
    )
    @EntityGraph(attributePaths = { "organization", "department", "reporter", "assignedEngineer" })
    List<Incident> findActiveByReporter(@Param("organizationId") UUID organizationId,
                                        @Param("reporterId") UUID reporterId,
                                        @Param("statuses") Collection<IncidentStatus> statuses);

    @Query(
            """
                    SELECT incident
                    FROM Incident incident
                    WHERE incident.organization.id = :organizationId
                    AND incident.assignedEngineer.id = :engineerId
                    AND incident.status IN (:statuses)
                    ORDER BY incident.createdAt DESC
                    """
    )
    @EntityGraph(attributePaths = { "organization", "department", "reporter", "assignedEngineer" })
    List<Incident> findActiveByAssignedEngineer(@Param("organizationId") UUID organizationId,
                                                @Param("engineerId") UUID engineerId,
                                                @Param("statuses") Collection<IncidentStatus> statuses);
}
