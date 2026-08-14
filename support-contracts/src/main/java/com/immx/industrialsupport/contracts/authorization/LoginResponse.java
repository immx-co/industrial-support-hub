package com.immx.industrialsupport.contracts.authorization;

import java.util.UUID;

/**
 * Модель результата успешной авторизации пользователя.
 *
 * @param accessToken    токен доступа авторизованного пользователя
 * @param tokenType      тип токена
 * @param userId         идентификатор пользователя
 * @param organizationId идентификатор организации пользователя
 * @param departmentId   идентификатор подразделения пользователя
 * @param username       логин пользователя
 */
public record LoginResponse(String accessToken,
                            String tokenType,
                            UUID userId,
                            UUID organizationId,
                            UUID departmentId,
                            String username) {
}
