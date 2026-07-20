package com.immx.industrialsupport.supportservice.dto.organization;

import java.time.OffsetDateTime;
import java.util.UUID;

public record OrganizationResponse(UUID id, String externalId, String name, OffsetDateTime createdAt,
                                   OffsetDateTime updatedAt) {
}
