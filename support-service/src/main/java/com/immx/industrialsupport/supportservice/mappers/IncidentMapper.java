package com.immx.industrialsupport.supportservice.mappers;

import com.immx.industrialsupport.contracts.incident.IncidentResponse;
import com.immx.industrialsupport.supportservice.entities.Incident;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Маппер модели обращения в модель обращения ответа сервиса.
 */
@Component
public class IncidentMapper {

    /**
     * Маппит модель обращения в модель обращения ответа сервиса.
     *
     * @param incident модель обращения для преобразования в модель обращения ответа сервиса
     * @return модель обращения ответа сервиса
     */
    public IncidentResponse toResponse(Incident incident) {
        return new IncidentResponse(
                incident.getId(),
                incident.getOrganization()
                        .getId(),
                incident.getDepartment()
                        .getId(),
                incident.getReporter()
                        .getId(),
                incident.getAssignedEngineer() == null ? null : incident.getAssignedEngineer()
                        .getId(),
                incident.getTitle(),
                incident.getDescription(),
                incident.getPriority(),
                incident.getStatus(),
                incident.getSlaDeadline(),
                incident.getResolvedAt(),
                incident.getClosedAt(),
                incident.getCreatedAt(),
                incident.getUpdatedAt(),
                incident.isSlaBreached(),
                incident.getSlaBreachedAt());
    }

    /**
     * Маппит список моделей обращений в модель списка обращений ответа сервиса.
     *
     * @param incidents список обращений для преобразования в модель списка обращений ответа сервиса
     * @return список обращений ответа сервиса
     */
    public List<IncidentResponse> toResponseList(List<Incident> incidents) {
        return incidents.stream()
                .map(this::toResponse)
                .toList();
    }
}
