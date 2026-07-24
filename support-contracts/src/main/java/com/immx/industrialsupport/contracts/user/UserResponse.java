package com.immx.industrialsupport.contracts.user;

import com.immx.industrialsupport.contracts.role.RoleName;

import java.time.OffsetDateTime;
import java.util.Set;
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
                           OffsetDateTime updatedAt,
                           Set<RoleName> roles) {
}
