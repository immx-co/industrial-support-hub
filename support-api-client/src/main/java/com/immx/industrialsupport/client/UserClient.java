package com.immx.industrialsupport.client;

import com.immx.industrialsupport.contracts.common.IndustrialSupportResponseData;
import com.immx.industrialsupport.contracts.user.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

/**
 * Клиент для работы с пользователями.
 */
public class UserClient {

    @Autowired
    @Qualifier("supportRestClient")
    private RestClient restClient;

    /**
     * Получает профиль текущего авторизованного пользователя.
     *
     * @param accessToken токен доступа
     * @return профиль текущего пользователя
     */
    public IndustrialSupportResponseData<UserResponse> getCurrentUser(String accessToken) {
        IndustrialSupportResponseData<UserResponse> response = restClient.get()
                .uri("/api/v1/users/me")
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if(response == null)
            throw new IllegalStateException("Support Service returned an empty user response.");

        return response;
    }
}
