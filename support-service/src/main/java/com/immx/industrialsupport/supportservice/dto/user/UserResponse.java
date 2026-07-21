package com.immx.industrialsupport.supportservice.dto.user;

import java.time.OffsetDateTime;
import java.util.UUID;

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
                           OffsetDateTime updatedAt) {
}
