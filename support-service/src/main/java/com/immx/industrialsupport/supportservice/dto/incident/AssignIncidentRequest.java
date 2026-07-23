package com.immx.industrialsupport.supportservice.dto.incident;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Модель для назначения инженера на выполнение обращения.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AssignIncidentRequest {

    @NotNull
    private UUID engineerId;
}
