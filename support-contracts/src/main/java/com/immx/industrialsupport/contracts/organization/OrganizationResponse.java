package com.immx.industrialsupport.contracts.organization;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Ответ модели организации.
 *
 * @param id         идентификатор организации
 * @param externalId внутренний идентификатор организации
 * @param name       название организации
 * @param createdAt  время создания организации
 * @param updatedAt  время обновления организации
 */
public record OrganizationResponse(UUID id,
                                   String externalId,
                                   String name,
                                   OffsetDateTime createdAt,
                                   OffsetDateTime updatedAt) {
}
