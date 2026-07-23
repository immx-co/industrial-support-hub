package com.immx.industrialsupport.supportservice.controllers;

import com.immx.industrialsupport.supportservice.dto.common.IndustrialSupportResponseData;
import com.immx.industrialsupport.supportservice.dto.role.RoleName;
import com.immx.industrialsupport.supportservice.dto.user.CreateUserRequest;
import com.immx.industrialsupport.supportservice.dto.user.UpdateUserRolesRequest;
import com.immx.industrialsupport.supportservice.dto.user.UserResponse;
import com.immx.industrialsupport.supportservice.entities.User;
import com.immx.industrialsupport.supportservice.mappers.UserMapper;
import com.immx.industrialsupport.supportservice.services.user.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Контроллер для работы с пользователями подразделений организации
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(
        name = "Users",
        description = "Работа с пользователями подразделений"
)
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private UserMapper userMapper;

    /**
     * Получает всех пользователей подразделения организации
     *
     * @param departmentId идентификатор подразделения организации
     * @return список пользователей подразделения организации
     */
    @GetMapping("/departments/{departmentId}")
    @Operation(
            summary = "Получает всех пользователей подразделения организации",
            description = "Возвращает список пользователей подразделения организации"
    )
    public ResponseEntity<IndustrialSupportResponseData<List<UserResponse>>> getAllUsersByDepartment(@PathVariable(
            "departmentId"
    ) UUID departmentId) {
        List<User> users = userService.getAllUsersByDepartmentId(departmentId);
        List<UserResponse> response = userMapper.toResponseList(users);

        return ResponseEntity.ok(new IndustrialSupportResponseData<>(
                "Список пользователей подразделения получен",
                response));
    }

    /**
     * Получает всех пользователей организации
     *
     * @param organizationId идентификатор организации
     * @return список пользователей организации
     */
    @GetMapping("/organizations/{organizationId}")
    @Operation(
            summary = "Получает всех пользователей организации",
            description = "Возвращает список пользователей подразделения организации"
    )
    public ResponseEntity<IndustrialSupportResponseData<List<UserResponse>>> getAllUsersByOrganization(@PathVariable(
            "organizationId"
    ) UUID organizationId) {
        List<User> users = userService.getAllUsersByOrganizationId(organizationId);
        List<UserResponse> response = userMapper.toResponseList(users);

        return ResponseEntity.ok(new IndustrialSupportResponseData<>(
                "Список пользователей организации получен",
                response));
    }

    @PostMapping("/departments/{departmentId}")
    @Operation(
            summary = "Создаёт пользователя подразделения",
            description = "Возвращает созданного пользователя подразделения"
    )
    public ResponseEntity<IndustrialSupportResponseData<UserResponse>> createUser(@PathVariable("departmentId") UUID departmentId,
                                                                                  @RequestBody @Valid CreateUserRequest createUserRequest) {
        User user = userService.create(
                departmentId,
                createUserRequest);
        UserResponse response = userMapper.toResponse(user);

        return ResponseEntity.ok(new IndustrialSupportResponseData<>(
                "Пользователь успешно создан",
                response));
    }

    @PutMapping("/{id}/roles")
    @Operation(
            summary = "Изменяет роли пользователя",
            description = "Возвращает пользователя с измененными ролями"
    )
    public ResponseEntity<IndustrialSupportResponseData<UserResponse>> updateUserRoles(@PathVariable("id") UUID id,
                                                                                       @RequestBody @Valid UpdateUserRolesRequest updateUserRolesRequest) {
        User user = userService.updateRoles(
                id,
                updateUserRolesRequest);
        UserResponse response = userMapper.toResponse(user);

        return ResponseEntity.ok(new IndustrialSupportResponseData<>(
                "Роли пользователя изменены",
                response));
    }

    /**
     * Получает коллекцию ролей пользователя по идентификатору.
     * @param id идентификатор пользователя
     * @return коллекция ролей пользователя
     */
    @GetMapping("/{id}/roles")
    @Operation(
            summary = "Получает коллекцию ролей пользователя по идентификатору",
            description = "Возвращает коллекцию ролей пользователя"
    )
    public ResponseEntity<IndustrialSupportResponseData<Set<RoleName>>> getUserRoles(@PathVariable("id") UUID id) {
        Set<RoleName> roles = userService.getRoles(id);

        return ResponseEntity.ok(new IndustrialSupportResponseData<>(
                "Коллекция ролей пользователя успешно получена",
                roles));
    }
}
