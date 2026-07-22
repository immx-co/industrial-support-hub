package com.immx.industrialsupport.supportservice.dto.user;

import com.immx.industrialsupport.supportservice.dto.role.RoleName;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Модель для обновления ролей у пользователя.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRolesRequest {

    @NotEmpty
    private Set<RoleName> roles;
}
