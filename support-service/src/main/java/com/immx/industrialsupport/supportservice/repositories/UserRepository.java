package com.immx.industrialsupport.supportservice.repositories;

import com.immx.industrialsupport.supportservice.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
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
                        SELECT DISTINCT user
                        FROM User user
                        JOIN FETCH user.department department
                        JOIN FETCH department.organization organization
                        LEFT JOIN FETCH user.roles roles
                        WHERE department.id = :departmentId
                    """
    )
    List<User> findAllByDepartment(@Param("departmentId") UUID departmentId);

    @Query(
            """
                        SELECT DISTINCT user
                        FROM User user
                        JOIN FETCH user.department department
                        JOIN FETCH department.organization organization
                        LEFT JOIN FETCH user.roles roles
                        WHERE organization.id = :organizationId
                    """
    )
    List<User> findAllByOrganization(@Param("organizationId") UUID organizationId);

    @Query(
            """
                        SELECT DISTINCT user
                        FROM User user
                        JOIN FETCH user.department department
                        JOIN FETCH department.organization organization
                        LEFT JOIN FETCH user.roles roles
                        WHERE user.id = :userId
                    """
    )
    Optional<User> findByIdWithRoles(@Param("userId") UUID userId);

    @EntityGraph(
            attributePaths = { "department", "department.organization", "roles" }
    )
    Optional<User> findByDepartment_IdAndUsernameIgnoreCase(UUID departmentId,
                                                            String username);

    boolean existsByDepartment_IdAndUsernameIgnoreCase(UUID departmentId,
                                                       String username);
}
