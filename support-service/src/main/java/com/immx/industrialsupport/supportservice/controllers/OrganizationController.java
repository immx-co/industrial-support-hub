package com.immx.industrialsupport.supportservice.controllers;

import com.immx.industrialsupport.contracts.common.IndustrialSupportResponseData;
import com.immx.industrialsupport.contracts.organization.CreateOrganizationRequest;
import com.immx.industrialsupport.contracts.organization.OrganizationResponse;
import com.immx.industrialsupport.supportservice.entities.Organization;
import com.immx.industrialsupport.supportservice.mappers.OrganizationMapper;
import com.immx.industrialsupport.supportservice.services.organization.IOrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер для работы с организациями.
 */
@RestController
@RequestMapping("/api/v1/organizations")
@Tag(
        name = "Organizations",
        description = "Работа с организациями"
)
public class OrganizationController {

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private OrganizationMapper organizationMapper;

    /**
     * Получить все организации.
     *
     * @return список всех организаций
     */
    @GetMapping()
    @Operation(
            summary = "Получить все организации",
            description = "Возвращает список всех организаций"
    )
    public ResponseEntity<IndustrialSupportResponseData<List<OrganizationResponse>>> getAllOrganizations() {
        List<Organization> organizations = organizationService.getAll();
        List<OrganizationResponse> response = organizationMapper.toResponseList(organizations);

        return ResponseEntity.ok(new IndustrialSupportResponseData<>(
                "Список организаций получен",
                response));
    }

    /**
     * Получить организацию по ее идентификатору.
     *
     * @param id идентификатор организации
     * @return организация по ее идентификатору
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Получить организацию по идентификатору",
            description = "Возвращает организацию по ее идентификатору"
    )
    public ResponseEntity<IndustrialSupportResponseData<OrganizationResponse>> getOrganization(@PathVariable("id") UUID id) {
        Organization organization = organizationService.getById(id);
        OrganizationResponse response = organizationMapper.toResponse(organization);

        return ResponseEntity.ok(new IndustrialSupportResponseData<>(
                "Организация по её идентификатору получена",
                response));
    }

    /**
     * Добавляет организацию.
     *
     * @param createOrganizationRequest тело запроса на добавление организации
     * @return добавленная организация
     */
    @PostMapping
    @Operation(
            summary = "Добавить организацию",
            description = "Возвращает добавленную организацию"
    )
    public ResponseEntity<IndustrialSupportResponseData<OrganizationResponse>> saveOrganization(@RequestBody @Valid CreateOrganizationRequest createOrganizationRequest) {
        Organization organization = organizationService.save(createOrganizationRequest);
        OrganizationResponse response = organizationMapper.toResponse(organization);

        return ResponseEntity.ok(new IndustrialSupportResponseData<>(
                "Организация успешно добавлена",
                response));
    }

    /**
     * Удаляет организацию по идентификатору.
     *
     * @param id идентификатор организации
     * @return удаленная организация
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаляет организацию по идентификатору",
            description = "Возвращает удаленную организацию"
    )
    public ResponseEntity<IndustrialSupportResponseData<OrganizationResponse>> deleteOrganizationById(@PathVariable(
            "id"
    ) UUID id) {
        Organization organization = organizationService.deleteById(id);
        OrganizationResponse response = organizationMapper.toResponse(organization);

        return ResponseEntity.ok(new IndustrialSupportResponseData<>(
                "Организация успешно удалена",
                response));
    }
}
