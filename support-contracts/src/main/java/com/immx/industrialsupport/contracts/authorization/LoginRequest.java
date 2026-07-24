package com.immx.industrialsupport.contracts.authorization;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotNull
    UUID departmentId;

    @NotBlank
    String username;

    @NotBlank
    String password;
}
