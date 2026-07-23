package com.immx.industrialsupport.supportservice.dto.incident;

import jakarta.validation.constraints.NotNull;

public record ChangeIncidentStatusRequest(@NotNull IncidentStatus status) {
}
