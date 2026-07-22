package com.immx.industrialsupport.supportservice.repositories;

import com.immx.industrialsupport.supportservice.dto.role.RoleName;
import com.immx.industrialsupport.supportservice.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с ролями пользователей.
 */
public interface RoleRepository extends JpaRepository<Role, UUID> {

    /**
     * Получает роль по названию роли.
     * @param name название роли
     * @return полученная роль
     */
    Optional<Role> findByName(RoleName name);

    /**
     * Получает все роли по коллекции названий ролей.
     * @param names коллекция названий ролей
     * @return список полученных ролей
     */
    List<Role> findAllByNameIn(Collection<RoleName> names);
}
