package com.immx.industrialsupport.supportservice.services.user;

import com.immx.industrialsupport.supportservice.dto.role.RoleName;
import com.immx.industrialsupport.supportservice.dto.user.CreateUserRequest;
import com.immx.industrialsupport.supportservice.dto.user.UpdateUserRolesRequest;
import com.immx.industrialsupport.supportservice.entities.User;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Интерфейс сервиса для работы с пользователями.
 */
public interface IUserService {

    /**
     * Получает всех пользователей подразделения конкретной организации.
     *
     * @param departmentId идентификатор подразделения
     * @return список пользователей
     */
    List<User> getAllUsersByDepartmentId(UUID departmentId);

    /**
     * Получает всех пользователей организации
     *
     * @param organizationId идентификатор организации
     * @return список пользователей конкретной организации
     */
    List<User> getAllUsersByOrganizationId(UUID organizationId);

    /**
     * Создаёт пользователя
     *
     * @param departmentId идентификатор подразделения
     * @param createUserRequest тело модели запроса на создание пользователя
     * @return созданный пользователь
     */
    User create(UUID departmentId, CreateUserRequest createUserRequest);

    /**
     * Обновляет роли у пользователя.
     * @param userId идентификатор пользователя, у которого следует обновить роли
     * @param updateUserRolesRequest тело запроса на обновление ролей
     * @return обновленный пользователь
     */
    User updateRoles(UUID userId, UpdateUserRolesRequest updateUserRolesRequest);

    /**
     * Получает коллекцию ролей пользователя.
     * @param userId идентификатор пользователя, список ролей которого получить
     * @return коллекция ролей пользователя
     */
    Set<RoleName> getRoles(UUID userId);
}
