package com.immx.industrialsupport.supportservice.services.department;

import com.immx.industrialsupport.contracts.department.CreateDepartmentRequest;
import com.immx.industrialsupport.supportservice.entities.Department;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс сервиса для работы с подразделениями организации.
 */
public interface IDepartmentService {

    /**
     * Получает подразделение конкретной организации.
     *
     * @param organizationId идентификатор организации
     * @param departmentId   идентификатор подразделения организации
     * @return подразделение конкретной организации
     */
    Department getById(UUID organizationId,
                       UUID departmentId);

    /**
     * Получает все подразделения конкретной организации.
     *
     * @param organizationId идентификатор организации
     * @return список подразделений конкретной организации
     */
    List<Department> getAllByOrganizationId(UUID organizationId);

    /**
     * Создает подразделение конкретной организации.
     *
     * @param organizationId          идентификатор организации
     * @param createDepartmentRequest модель запроса на создание подразделения конкретной организации
     * @return созданное подразделение
     */
    Department create(UUID organizationId,
                      CreateDepartmentRequest createDepartmentRequest);
}
