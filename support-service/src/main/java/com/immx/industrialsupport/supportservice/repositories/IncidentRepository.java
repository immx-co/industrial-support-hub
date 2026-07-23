package com.immx.industrialsupport.supportservice.repositories;

import com.immx.industrialsupport.supportservice.entities.Incident;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
