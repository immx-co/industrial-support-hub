package com.immx.industrialsupport.supportservice.dto.department;

import java.time.OffsetDateTime;
import java.util.UUID;

public record DepartmentResponseWithoutId(String externalId, String name, OffsetDateTime createdAt,
                                          OffsetDateTime updatedAt) {
}
