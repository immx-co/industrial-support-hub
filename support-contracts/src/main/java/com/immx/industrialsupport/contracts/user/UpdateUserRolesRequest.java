package com.immx.industrialsupport.contracts.user;

import com.immx.industrialsupport.contracts.role.RoleName;
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
