package com.immx.industrialsupport.supportservice.mappers;

import com.immx.industrialsupport.supportservice.dto.department.DepartmentResponse;
import com.immx.industrialsupport.supportservice.dto.department.DepartmentResponseWithoutId;
import com.immx.industrialsupport.supportservice.entities.Department;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Маппер модели подразделения организации в модель ответа сервиса.
 */
@Component
public class DepartmentMapper {

    /**
     * Маппит модель подразделения организации в модель ответа сервиса.
     *
     * @param department модель подразделения организации для преобразования в модель ответа сервиса
     * @return модель ответа сервиса
     */
    public DepartmentResponse toResponse(Department department) {
        return new DepartmentResponse(
                department.getId(),
                department.getOrganization()
                        .getId(),
                department.getExternalId(),
                department.getName(),
                department.getCreatedAt(),
                department.getUpdatedAt());
    }

    /**
     * Маппит список моделей подразделений организации в модель ответа сервиса.
     *
     * @param departments список подразделений организации для преобразования в модель ответа сервиса
     * @return модель ответа сервиса
     */
    public List<DepartmentResponse> toResponseList(List<Department> departments) {
        return departments.stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Маппит модель подразделения организации в модель ответа сервиса <code>DepartmentResponseWithoutId</code>.
     *
     * @param department модель подразделения организации для преобразования в модель ответа сервиса
     *                   <code>DepartmentResponseWithoutId</code>
     * @return модель ответа сервиса
     */
    public DepartmentResponseWithoutId toResponseWithoutId(Department department) {
        return new DepartmentResponseWithoutId(
                department.getExternalId(),
                department.getName(),
                department.getCreatedAt(),
                department.getUpdatedAt());
    }
}
