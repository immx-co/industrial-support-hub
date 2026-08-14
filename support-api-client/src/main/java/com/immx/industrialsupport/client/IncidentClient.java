package com.immx.industrialsupport.client;

import com.immx.industrialsupport.contracts.common.IndustrialSupportResponseData;
import com.immx.industrialsupport.contracts.incident.CreateIncidentRequest;
import com.immx.industrialsupport.contracts.incident.IncidentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.UUID;

/**
 * Клиент для работы с обращениями.
 */
public class IncidentClient {

    @Autowired
    @Qualifier("supportRestClient")
    private RestClient restClient;

    /**
     * Создаёт новое обращение в указанной организации.
     *
     * @param organizationId идентификатор организации
     * @param request        данные создаваемого обращения
     * @param accessToken    токен текущего пользователя
     * @return ответ сервиса с созданным обращением.
     */
    public IndustrialSupportResponseData<IncidentResponse> createIncident(UUID organizationId,
                                                                          CreateIncidentRequest request,
                                                                          String accessToken) {
        IndustrialSupportResponseData<IncidentResponse> response = restClient.post()
                .uri(
                        "/api/v1/organizations/{organizationId}/incidents",
                        organizationId)
                .headers(headers -> headers.setBearerAuth(accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if(response == null)
            throw new IllegalStateException("Support Service returned an empty incident response.");

        return response;
    }
}
