package com.immx.industrialsupport.supportservice.mappers;

import com.immx.industrialsupport.contracts.organization.OrganizationResponse;
import com.immx.industrialsupport.supportservice.entities.Organization;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Маппер модели организации в модель ответа сервиса.
 */
@Component
public class OrganizationMapper {

    /**
     * Маппит модель организации в модель ответа сервиса.
     *
     * @param organization модель организации для преобразования в модель ответа сервиса
     * @return модель ответа сервиса
     */
    public OrganizationResponse toResponse(Organization organization) {
        return new OrganizationResponse(
                organization.getId(),
                organization.getExternalId(),
                organization.getName(),
                organization.getCreatedAt(),
                organization.getUpdatedAt());
    }

    /**
     * Маппит список моделей организаций в модель ответа сервиса.
     *
     * @param organizations список моделей организации для преобразования в модель ответа сервиса
     * @return модель ответа сервиса
     */
    public List<OrganizationResponse> toResponseList(List<Organization> organizations) {
        return organizations.stream()
                .map(this::toResponse)
                .toList();
    }
}
