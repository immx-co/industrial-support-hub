package com.immx.industrialsupport.supportservice.dto.department;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Модель для создания подразделения.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDepartmentRequest {

    @Size(max = 100)
    private String externalId;

    @NotBlank
    @Size(max = 255)
    private String name;
}
