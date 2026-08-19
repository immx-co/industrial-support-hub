package com.immx.industrialsupport.client;

import com.immx.industrialsupport.contracts.common.IndustrialSupportResponseData;
import com.immx.industrialsupport.contracts.incident.CreateIncidentRequest;
import com.immx.industrialsupport.contracts.incident.IncidentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.List;

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
     * @param request     данные создаваемого обращения
     * @param accessToken токен текущего пользователя
     * @return ответ сервиса с созданным обращением.
     */
    public IndustrialSupportResponseData<IncidentResponse> createIncident(CreateIncidentRequest request,
                                                                          String accessToken) {
        IndustrialSupportResponseData<IncidentResponse> response = restClient.post()
                .uri("/api/v1/incidents")
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

    /**
     * Получает активные обращения, фильтруя по идентификатору организации, подразделения, пользователя и ролей.
     *
     * @param accessToken токен текущего пользователя
     * @return ответ сервиса с активными отфильтрованными обращениями.
     */
    public IndustrialSupportResponseData<List<IncidentResponse>> getActiveIncidents(String accessToken) {
        IndustrialSupportResponseData<List<IncidentResponse>> response = restClient.get()
                .uri("/api/v1/incidents/active")
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if(response == null)
            throw new IllegalStateException("Support Service returned an empty active incidents response.");

        return response;
    }
}
