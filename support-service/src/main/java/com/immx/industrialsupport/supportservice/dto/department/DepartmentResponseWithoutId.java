package com.immx.industrialsupport.supportservice.dto.department;

import java.time.OffsetDateTime;

public record DepartmentResponseWithoutId(String externalId, String name, OffsetDateTime createdAt,
                                          OffsetDateTime updatedAt) {
}
