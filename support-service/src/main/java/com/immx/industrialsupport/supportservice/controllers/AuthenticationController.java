package com.immx.industrialsupport.supportservice.controllers;

import com.immx.industrialsupport.contracts.authorization.LoginRequest;
import com.immx.industrialsupport.contracts.authorization.LoginResponse;
import com.immx.industrialsupport.supportservice.services.authorization.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для работы с авторизацией.
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(
            summary = "Авторизует пользователя"
    )
    @SecurityRequirements
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(authenticationService.login(loginRequest));
    }
}
