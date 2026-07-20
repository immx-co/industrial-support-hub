package com.immx.industrialsupport.supportservice.repositories;

import com.immx.industrialsupport.supportservice.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с подразделениями.
 */
public interface DepartmentRepository extends JpaRepository<Department, UUID> {

    @Query(
            """
                    SELECT department FROM Department department WHERE department.organization.id = :organizationId
                    """
    )
    List<Department> findAllByOrganizationId(@Param("organizationId") UUID organizationId);

    @Query(
            """
                    SELECT department FROM Department department WHERE department.id = :departmentId AND department.organization.id = :organizationId
                    """
    )
    Optional<Department> findByIdAndOrganizationId(@Param("organizationId") UUID organizationId, @Param(
            "departmentId"
    ) UUID departmentId);
}
