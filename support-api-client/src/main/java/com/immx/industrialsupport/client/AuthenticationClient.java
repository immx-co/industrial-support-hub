package com.immx.industrialsupport.client;

import com.immx.industrialsupport.contracts.authorization.LoginRequest;
import com.immx.industrialsupport.contracts.authorization.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.UUID;

/**
 * Клиент для работы с аутентификацией и авторизацией пользователей.
 */
public class AuthenticationClient {

    @Autowired
    @Qualifier("supportRestClient")
    private RestClient restClient;

    public LoginResponse login(UUID departmentId,
                               String username,
                               String password) {
        LoginRequest loginRequest = new LoginRequest(
                departmentId,
                username,
                password);

        LoginResponse response = restClient.post()
                .uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(loginRequest)
                .retrieve()
                .body(LoginResponse.class);

        if(response == null)
            throw new IllegalStateException("Support Service returned an empty login response");

        return response;
    }
}
