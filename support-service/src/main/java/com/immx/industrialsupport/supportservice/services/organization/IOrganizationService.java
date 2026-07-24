package com.immx.industrialsupport.supportservice.services.organization;

import com.immx.industrialsupport.contracts.organization.CreateOrganizationRequest;
import com.immx.industrialsupport.supportservice.entities.Organization;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс сервиса для работы с организациями.
 */
public interface IOrganizationService {

    /**
     * Получает все организации.
     *
     * @return список всех организаций
     */
    List<Organization> getAll();

    /**
     * Получает организацию по ее идентификатору.
     *
     * @param id идентификатор организации
     * @return полученная организация по идентификатору
     */
    Organization getById(UUID id);

    /**
     * Получает организацию по имени.
     *
     * @param organizationName имя организации
     * @return полученная организация по имени
     */
    Organization getByName(String organizationName);

    /**
     * Создаёт организацию.
     *
     * @param createOrganizationRequest модель запроса на создание организации
     * @return созданная организация
     */
    Organization save(CreateOrganizationRequest createOrganizationRequest);

    /**
     * Удаляет организацию по её идентификатору.
     *
     * @param id идентификатор организации
     * @return удалённая организация по идентификатору
     */
    Organization deleteById(UUID id);
}
