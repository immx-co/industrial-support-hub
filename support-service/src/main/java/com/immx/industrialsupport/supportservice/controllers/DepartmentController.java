package com.immx.industrialsupport.supportservice.controllers;

import com.immx.industrialsupport.supportservice.dto.common.IndustrialSupportResponseData;
import com.immx.industrialsupport.supportservice.dto.department.CreateDepartmentRequest;
import com.immx.industrialsupport.supportservice.dto.department.DepartmentResponse;
import com.immx.industrialsupport.supportservice.dto.department.DepartmentResponseWithoutId;
import com.immx.industrialsupport.supportservice.entities.Department;
import com.immx.industrialsupport.supportservice.mappers.DepartmentMapper;
import com.immx.industrialsupport.supportservice.services.department.IDepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер для работы с подразделениями организации.
 */
@RestController
@RequestMapping("/api/v1/organizations/{organizationId}/departments")
public class DepartmentController {

    @Autowired
    private IDepartmentService departmentService;

    @Autowired
    private DepartmentMapper departmentMapper;

    @GetMapping
    @Operation(
            summary = "Получает все подразделения конкретной организации",
            description = "Возвращает список подразделений конкретной организации"
    )
    public ResponseEntity<IndustrialSupportResponseData<List<DepartmentResponse>>> getAllDepartments(@PathVariable(
            "organizationId"
    ) UUID organizationId) {
        List<Department> departments = departmentService.getAllByOrganizationId(organizationId);
        List<DepartmentResponse> response = departmentMapper.toResponseList(departments);

        return ResponseEntity.ok(new IndustrialSupportResponseData<>(
                "Список всех подразделений организации получен",
                response));
    }

    @PostMapping
    @Operation(
            summary = "Добавляет подразделение в конкретную организацию",
            description = "Возвращает добавленное подразделение"
    )
    public ResponseEntity<IndustrialSupportResponseData<DepartmentResponse>> saveDepartment(@PathVariable(
            "organizationId"
    ) UUID organizationId, @RequestBody CreateDepartmentRequest createDepartmentRequest) {
        Department department = departmentService.create(
                organizationId,
                createDepartmentRequest);
        DepartmentResponse response = departmentMapper.toResponse(department);

        return ResponseEntity.ok(new IndustrialSupportResponseData<>(
                "Подразделение конкретной организации успешно создано",
                response));
    }

    @GetMapping("/{departmentId}")
    @Operation(
            summary = "Получает подразделение конкретное организации",
            description = "Возвращает подразделение конкретной организации"
    )
    public ResponseEntity<IndustrialSupportResponseData<DepartmentResponseWithoutId>> getDepartment(@PathVariable(
            "organizationId"
    ) UUID organizationId, @PathVariable(
            "departmentId"
    ) UUID departmentId) {
        Department department = departmentService.getById(
                organizationId,
                departmentId);
        DepartmentResponseWithoutId response = departmentMapper.toResponseWithoutId(department);

        return ResponseEntity.ok(new IndustrialSupportResponseData<>(
                "Подразделение конкретной организации успешно получено",
                response));
    }
}
