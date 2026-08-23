package com.immx.industrialsupport.contracts.authorization;

import com.immx.industrialsupport.contracts.role.RoleName;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

/**
 * Модель результата успешной авторизации пользователя.
 *
 * @param accessToken    токен доступа авторизованного пользователя
 * @param tokenType      тип токена
 * @param expiresAt      Момент окончания действия токена
 * @param userId         идентификатор пользователя
 * @param organizationId идентификатор организации пользователя
 * @param departmentId   идентификатор подразделения пользователя
 * @param username       логин пользователя
 * @param roles          роли пользователя
 */
public record LoginResponse(String accessToken,
                            String tokenType,
                            Instant expiresAt,
                            UUID userId,
                            UUID organizationId,
                            UUID departmentId,
                            String username,
                            Set<RoleName> roles) {
}
