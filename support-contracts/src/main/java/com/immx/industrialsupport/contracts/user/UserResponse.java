package com.immx.industrialsupport.contracts.user;

import com.immx.industrialsupport.contracts.role.RoleName;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * Модель API ответа пользователя.
 *
 * @param id               идентификатор пользователя
 * @param organizationId   идентификатор организации пользователя
 * @param departmentId     идентификатор подразделения пользователя
 * @param externalId       внутренний идентификатор пользователя
 * @param username         пользовательское имя пользователя
 * @param email            электронная почта пользователя
 * @param firstName        имя пользователя
 * @param lastName         фамилия пользователя
 * @param enabled          флаг активности пользователя
 * @param createdAt        дата и время создания пользователя
 * @param updatedAt        дата и время обновления пользователя
 * @param roles            принадлежащие роли пользователя
 * @param telegramUsername имя пользователя в телеграме
 */
public record UserResponse(UUID id,
                           UUID organizationId,
                           UUID departmentId,
                           String externalId,
                           String username,
                           String email,
                           String firstName,
                           String lastName,
                           boolean enabled,
                           OffsetDateTime createdAt,
                           OffsetDateTime updatedAt,
                           Set<RoleName> roles,
                           String telegramUsername) {
}
