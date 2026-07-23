package com.immx.industrialsupport.supportservice.dto.incident;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Модель для создания обращения.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateIncidentRequest {

    @NotNull
    private UUID departmentId;

    @NotNull
    private UUID reporterId;

    @NotBlank
    @Size(max = 200)
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private IncidentPriority priority;
}
