package com.immx.industrialsupport.supportservice.controllers;

import com.immx.industrialsupport.contracts.common.IndustrialSupportResponseData;
import com.immx.industrialsupport.contracts.incident.AssignIncidentRequest;
import com.immx.industrialsupport.contracts.incident.ChangeIncidentStatusRequest;
import com.immx.industrialsupport.contracts.incident.CreateIncidentRequest;
import com.immx.industrialsupport.contracts.incident.IncidentResponse;
import com.immx.industrialsupport.supportservice.entities.Incident;
import com.immx.industrialsupport.supportservice.mappers.IncidentMapper;
import com.immx.industrialsupport.supportservice.services.incident.IIncidentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер для работы с обращениями.
 */
@RestController
@RequestMapping("/api/v1/organizations/{organizationId}/incidents")
@Tag(
        name = "Incidents",
        description = "Работа с обращениями"
)
public class IncidentController {

    @Autowired
    private IIncidentService incidentService;

    @Autowired
    private IncidentMapper incidentMapper;

    /**
     * Создаёт обращение
     *
     * @param organizationId        идентификатор организации, в которой создается обращение
     * @param createIncidentRequest запрос на создание обращения
     * @return созданное обращение
     */
    @PostMapping
    @Operation(
            summary = "Создаёт обращение",
            description = "Возвращает созданное обращение"
    )
    public ResponseEntity<IndustrialSupportResponseData<IncidentResponse>> create(@PathVariable("organizationId") UUID organizationId,
                                                                                  @RequestBody @Valid CreateIncidentRequest createIncidentRequest) {
        Incident incident = incidentService.create(
                organizationId,
                createIncidentRequest);
        IncidentResponse response = incidentMapper.toResponse(incident);

        return ResponseEntity.ok(new IndustrialSupportResponseData<>(
                "Обращение успешно создано",
                response));
    }

    /**
     * Получает все обращения организации
     *
     * @param organizationId идентификатор организации, список обращений из которой получить
     * @return список обращений организации
     */
    @GetMapping
    @Operation(
            summary = "Получает все обращения организации",
            description = "Возвращает список всех обращений организации"
    )
    public ResponseEntity<IndustrialSupportResponseData<List<IncidentResponse>>> getAll(@PathVariable(
            "organizationId"
    ) UUID organizationId) {
        List<Incident> incidents = incidentService.getAllByOrganization(organizationId);
        List<IncidentResponse> response = incidentMapper.toResponseList(incidents);

        return ResponseEntity.ok(new IndustrialSupportResponseData<>(
                "Список всех обращений организации успешно получен",
                response));
    }

    /**
     * Получает обращение по идентификатору.
     *
     * @param organizationId идентификатор организации, обращение по идентификатору из которой получить
     * @param incidentId     идентификатор обращения
     * @return полученное обращение по идентификатору
     */
    @GetMapping("/{incidentId}")
    @Operation(
            summary = "Получает обращение по идентификатору",
            description = "Возвращает обращение по идентификатору"
    )
    public ResponseEntity<IndustrialSupportResponseData<IncidentResponse>> get(@PathVariable("organizationId") UUID organizationId,
                                                                               @PathVariable("incidentId") UUID incidentId) {
        Incident incident = incidentService.getById(
                organizationId,
                incidentId);
        IncidentResponse response = incidentMapper.toResponse(incident);

        return ResponseEntity.ok(new IndustrialSupportResponseData<>(
                "Обращение по идентификатору успешно получено",
                response));
    }

    /**
     * Назначает инженера на обращение.
     *
     * @param organizationId        идентификатор организации, в которой назначить инженера на обращение
     * @param incidentId            идентификатор обращения, которое назначить на инженера
     * @param assignIncidentRequest запрос на назначение инженера на обращение
     * @return назначенное на инженера обращение
     */
    @PatchMapping("/{incidentId}/assignment")
    @Operation(
            summary = "Назначает инженера на обращение",
            description = "Назначенное на инженера обращение"
    )
    public ResponseEntity<IndustrialSupportResponseData<IncidentResponse>> assignEngineer(@PathVariable(
                                                                                                  "organizationId"
                                                                                          ) UUID organizationId,
                                                                                          @PathVariable("incidentId") UUID incidentId,
                                                                                          @RequestBody @Valid AssignIncidentRequest assignIncidentRequest) {
        Incident incident = incidentService.assignEngineer(
                organizationId,
                incidentId,
                assignIncidentRequest);
        IncidentResponse response = incidentMapper.toResponse(incident);

        return ResponseEntity.ok(new IndustrialSupportResponseData<>(
                "Инженер успешно назначен на обращение",
                response));
    }

    /**
     * Изменяет статус обращения.
     *
     * @param organizationId идентификатор организации, статус обращения в которой нужно изменить
     * @param incidentId     идентификатор обращения, статус которого изменить
     * @param request        запрос на изменение статуса обращения
     * @return обращение с измененным статусом
     */
    @PatchMapping("/{incidentId}/status")
    @Operation(
            summary = "Изменяет статус обращения",
            description = "Возвращает обращение с измененным статусом"
    )
    public ResponseEntity<IndustrialSupportResponseData<IncidentResponse>> changeStatus(@PathVariable UUID organizationId,
                                                                                        @PathVariable UUID incidentId,
                                                                                        @RequestBody @Valid ChangeIncidentStatusRequest request) {
        Incident incident = incidentService.changeStatus(
                organizationId,
                incidentId,
                request);
        IncidentResponse response = incidentMapper.toResponse(incident);

        return ResponseEntity.ok(new IndustrialSupportResponseData<>(
                "Статус обращения успешно изменен",
                response));
    }
}
