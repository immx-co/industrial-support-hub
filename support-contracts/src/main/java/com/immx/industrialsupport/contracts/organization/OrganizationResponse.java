package com.immx.industrialsupport.contracts.organization;

import java.time.OffsetDateTime;
import java.util.UUID;

public record OrganizationResponse(UUID id,
                                   String externalId,
                                   String name,
                                   OffsetDateTime createdAt,
                                   OffsetDateTime updatedAt) {
}
