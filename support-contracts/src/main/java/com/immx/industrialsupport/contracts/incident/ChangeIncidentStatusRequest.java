package com.immx.industrialsupport.contracts.incident;

import jakarta.validation.constraints.NotNull;

public record ChangeIncidentStatusRequest(@NotNull IncidentStatus status) {
}
