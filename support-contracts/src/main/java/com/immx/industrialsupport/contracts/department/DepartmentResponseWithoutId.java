package com.immx.industrialsupport.contracts.department;

import java.time.OffsetDateTime;

public record DepartmentResponseWithoutId(String externalId,
                                          String name,
                                          OffsetDateTime createdAt,
                                          OffsetDateTime updatedAt) {
}
