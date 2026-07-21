package com.immx.industrialsupport.supportservice.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Модель для создания пользователя.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    @Size(max = 100)
    private String externalId;

    @NotBlank
    @Size(min = 3, max = 100)
    private String username;

    @NotBlank
    @Size(max = 255)
    private String email;

    @NotBlank
    @Size(max = 100)
    private String password;

    @NotBlank
    @Size(max = 100)
    private String firstName;

    private String lastName;
}
