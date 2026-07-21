package com.immx.industrialsupport.supportservice.repositories;

import com.immx.industrialsupport.supportservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с пользователями.
 */
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query(
            """
                        SELECT user
                        FROM User user
                        JOIN FETCH user.department department
                        JOIN FETCH department.organization organization
                        WHERE department.id = :departmentId
                    """
    )
    List<User> findAllByDepartment(@Param("departmentId") UUID departmentId);

    @Query(
            """
                        SELECT user
                        FROM User user
                        JOIN FETCH user.department department
                        JOIN FETCH department.organization organization
                        WHERE organization.id = :organizationId
                    """
    )
    List<User> findAllByOrganization(@Param("organizationId") UUID organizationId);

    Optional<User> findByDepartment_IdAndUsernameIgnoreCase(UUID departmentId, String username);

    boolean existsByDepartment_IdAndUsernameIgnoreCase(UUID departmentId, String username);
}
