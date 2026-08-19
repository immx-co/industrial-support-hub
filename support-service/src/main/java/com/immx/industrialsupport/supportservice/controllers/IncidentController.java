package com.immx.industrialsupport.supportservice.controllers;

import com.immx.industrialsupport.contracts.common.IndustrialSupportResponseData;
import com.immx.industrialsupport.contracts.incident.AssignIncidentRequest;
import com.immx.industrialsupport.contracts.incident.ChangeIncidentStatusRequest;
import com.immx.industrialsupport.contracts.incident.CreateIncidentRequest;
import com.immx.industrialsupport.contracts.incident.IncidentResponse;
import com.immx.industrialsupport.supportservice.entities.Incident;
import com.immx.industrialsupport.supportservice.mappers.IncidentMapper;
import com.immx.industrialsupport.supportservice.security.currentuser.AuthenticatedUserContext;
import com.immx.industrialsupport.supportservice.security.currentuser.AuthenticatedUserContextProvider;
import com.immx.industrialsupport.supportservice.services.incident.IIncidentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер для работы с обращениями.
 */
@RestController
@RequestMapping("/api/v1/incidents")
@Tag(
        name = "Incidents",
        description = "Работа с обращениями"
)
public class IncidentController {

    @Autowired
    private IIncidentService incidentService;

    @Autowired
    private IncidentMapper incidentMapper;

    @Autowired
    private AuthenticatedUserContextProvider currentUserProvider;

    /**
     * Создаёт обращение
     *
     * @param createIncidentRequest запрос на создание обращения
     * @return созданное обращение
     */
    @PreAuthorize(
            """
                        hasAnyAuthority(
                            'ROLE_EMPLOYEE',
                            'ROLE_DISPATCHER',
                            'ROLE_ENGINEER',
                            'ROLE_MANAGER',
                            'ROLE_ADMIN'
                        )
                    """
    )
    @PostMapping
    @Operation(
            summary = "Создаёт обращение",
            description = "Возвращает созданное обращение"
    )
    public ResponseEntity<IndustrialSupportResponseData<IncidentResponse>> create(@RequestBody @Valid CreateIncidentRequest createIncidentRequest) {
        AuthenticatedUserContext currentUser = currentUserProvider.getCurrentUser();

        Incident incident = incidentService.create(
                currentUser.organizationId(),
                currentUser.departmentId(),
                currentUser.userId(),
                createIncidentRequest);
        IncidentResponse response = incidentMapper.toResponse(incident);

        return ResponseEntity.ok(new IndustrialSupportResponseData<>(
                "Обращение успешно создано",
                response));
    }

    /**
     * Получает все обращения организации
     *
     * @return список обращений организации
     */
    @PreAuthorize(
            """
                    hasAnyAuthority(
                        'ROLE_MANAGER',
                        'ROLE_ADMIN'
                    )
                    """
    )
    @GetMapping
    @Operation(
            summary = "Получает все обращения организации",
            description = "Возвращает список всех обращений организации"
    )
    public ResponseEntity<IndustrialSupportResponseData<List<IncidentResponse>>> getAll() {
        AuthenticatedUserContext currentUser = currentUserProvider.getCurrentUser();

        List<Incident> incidents = incidentService.getAllByOrganization(currentUser.organizationId());
        List<IncidentResponse> response = incidentMapper.toResponseList(incidents);

        return ResponseEntity.ok(new IndustrialSupportResponseData<>(
                "Список всех обращений организации успешно получен",
                response));
    }

    /**
     * Получает обращение по идентификатору.
     *
     * @param incidentId идентификатор обращения
     * @return полученное обращение по идентификатору
     */
    @GetMapping("/{incidentId}")
    @Operation(
            summary = "Получает обращение по идентификатору",
            description = "Возвращает обращение по идентификатору"
    )
    public ResponseEntity<IndustrialSupportResponseData<IncidentResponse>> get(@PathVariable("incidentId") UUID incidentId) {
        AuthenticatedUserContext currentUser = currentUserProvider.getCurrentUser();

        Incident incident = incidentService.getById(
                currentUser.organizationId(),
                incidentId);
        IncidentResponse response = incidentMapper.toResponse(incident);

        return ResponseEntity.ok(new IndustrialSupportResponseData<>(
                "Обращение по идентификатору успешно получено",
                response));
    }

    /**
     * Назначает инженера на обращение.
     *
     * @param incidentId            идентификатор обращения, которое назначить на инженера
     * @param assignIncidentRequest запрос на назначение инженера на обращение
     * @return назначенное на инженера обращение
     */
    @PreAuthorize(
            """
                    hasAnyAuthority(
                        'ROLE_DISPATCHER',
                        'ROLE_ADMIN'
                    )
                    """
    )
    @PatchMapping("/{incidentId}/assignment")
    @Operation(
            summary = "Назначает инженера на обращение",
            description = "Назначенное на инженера обращение"
    )
    public ResponseEntity<IndustrialSupportResponseData<IncidentResponse>> assignEngineer(@PathVariable("incidentId") UUID incidentId,
                                                                                          @RequestBody @Valid AssignIncidentRequest assignIncidentRequest) {
        AuthenticatedUserContext currentUser = currentUserProvider.getCurrentUser();

        Incident incident = incidentService.assignEngineer(
                currentUser.organizationId(),
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
     * @param incidentId идентификатор обращения, статус которого изменить
     * @param request    запрос на изменение статуса обращения
     * @return обращение с измененным статусом
     */
    @PreAuthorize(
            """
                    hasAnyAuthority(
                        'ROLE_EMPLOYEE',
                        'ROLE_DISPATCHER',
                        'ROLE_ENGINEER',
                        'ROLE_ADMIN'
                    )
                    """
    )
    @PatchMapping("/{incidentId}/status")
    @Operation(
            summary = "Изменяет статус обращения",
            description = "Возвращает обращение с измененным статусом"
    )
    public ResponseEntity<IndustrialSupportResponseData<IncidentResponse>> changeStatus(@PathVariable UUID incidentId,
                                                                                        @RequestBody @Valid ChangeIncidentStatusRequest request) {
        AuthenticatedUserContext currentUser = currentUserProvider.getCurrentUser();

        Incident incident = incidentService.changeStatus(
                currentUser.organizationId(),
                incidentId,
                request);
        IncidentResponse response = incidentMapper.toResponse(incident);

        return ResponseEntity.ok(new IndustrialSupportResponseData<>(
                "Статус обращения успешно изменен",
                response));
    }

    /**
     * Удаляет все обращения из базы данных.
     *
     * @return количество удалённых обращений
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping
    @Operation(
            summary = "Удаляет все обращения",
            description = "Безвозвратно удаляет все обращения всех организаций"
    )
    public ResponseEntity<IndustrialSupportResponseData<Long>> deleteAll() {
        long deletedCount = incidentService.deleteAll();

        return ResponseEntity.ok(new IndustrialSupportResponseData<>(
                "Все обращения успешно удалены",
                deletedCount));
    }

    /**
     * Получает доступные активные обращения.
     *
     * @return доступные активные обращения, отфильтрованные относительно роли запрашиваемого.
     */
    @PreAuthorize(
            """
                    hasAnyAuthority(
                        'ROLE_EMPLOYEE',
                        'ROLE_DISPATCHER',
                        'ROLE_ENGINEER',
                        'ROLE_MANAGER',
                        'ROLE_ADMIN'
                    )
                    """
    )
    @GetMapping("/active")
    @Operation(
            summary = "Получает доступные активные обращения",
            description = "Область видимости обращений определяется ролями текущего пользователя"
    )
    public ResponseEntity<IndustrialSupportResponseData<List<IncidentResponse>>> getActive() {
        AuthenticatedUserContext currentUser = currentUserProvider.getCurrentUser();

        List<Incident> incidents = incidentService.getActiveForUser(
                currentUser.organizationId(),
                currentUser.userId(),
                currentUser.roles());

        List<IncidentResponse> response = incidentMapper.toResponseList(incidents);

        return ResponseEntity.ok(new IndustrialSupportResponseData<>(
                "Список активных обращений успешно получен",
                response));
    }
}
