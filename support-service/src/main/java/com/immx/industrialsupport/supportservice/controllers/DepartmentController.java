package com.immx.industrialsupport.supportservice.controllers;

import com.immx.industrialsupport.contracts.common.IndustrialSupportResponseData;
import com.immx.industrialsupport.contracts.department.CreateDepartmentRequest;
import com.immx.industrialsupport.contracts.department.DepartmentResponse;
import com.immx.industrialsupport.contracts.department.DepartmentResponseWithoutId;
import com.immx.industrialsupport.supportservice.entities.Department;
import com.immx.industrialsupport.supportservice.mappers.DepartmentMapper;
import com.immx.industrialsupport.supportservice.security.currentuser.AuthenticatedUserContext;
import com.immx.industrialsupport.supportservice.security.currentuser.AuthenticatedUserContextProvider;
import com.immx.industrialsupport.supportservice.services.department.IDepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер для работы с подразделениями организации.
 */
@RestController
@RequestMapping("/api/v1/departments")
@Tag(
        name = "Departments",
        description = "Работа с подразделениями организаций"
)
public class DepartmentController {

    @Autowired
    private IDepartmentService departmentService;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private AuthenticatedUserContextProvider currentUserProvider;

    @GetMapping
    @Operation(
            summary = "Получает все подразделения организации",
            description = "Возвращает список подразделений организации"
    )
    public ResponseEntity<IndustrialSupportResponseData<List<DepartmentResponse>>> getAllDepartments() {
        AuthenticatedUserContext currentUser = currentUserProvider.getCurrentUser();

        List<Department> departments = departmentService.getAllByOrganizationId(currentUser.organizationId());
        List<DepartmentResponse> response = departmentMapper.toResponseList(departments);

        return ResponseEntity.ok(new IndustrialSupportResponseData<>(
                "Список всех подразделений организации " + currentUser.organizationId() + " получен",
                response));
    }

    @PostMapping
    @Operation(
            summary = "Добавляет подразделение",
            description = "Возвращает добавленное подразделение"
    )
    public ResponseEntity<IndustrialSupportResponseData<DepartmentResponse>> saveDepartment(@RequestBody @Valid CreateDepartmentRequest createDepartmentRequest) {
        AuthenticatedUserContext currentUser = currentUserProvider.getCurrentUser();

        Department department = departmentService.create(
                currentUser.organizationId(),
                createDepartmentRequest);
        DepartmentResponse response = departmentMapper.toResponse(department);

        return ResponseEntity.ok(new IndustrialSupportResponseData<>(
                "Подразделение организации " + currentUser.organizationId() + " успешно создано",
                response));
    }

    @GetMapping("/{departmentId}")
    @Operation(
            summary = "Получает подразделение конкретной организации",
            description = "Возвращает подразделение конкретной организации"
    )
    public ResponseEntity<IndustrialSupportResponseData<DepartmentResponseWithoutId>> getDepartment(@PathVariable(
            "departmentId"
    ) UUID departmentId) {
        AuthenticatedUserContext currentUser = currentUserProvider.getCurrentUser();

        Department department = departmentService.getById(
                currentUser.organizationId(),
                departmentId);
        DepartmentResponseWithoutId response = departmentMapper.toResponseWithoutId(department);

        return ResponseEntity.ok(new IndustrialSupportResponseData<>(
                "Подразделение организации " + currentUser.organizationId() + " успешно получено",
                response));
    }
}
