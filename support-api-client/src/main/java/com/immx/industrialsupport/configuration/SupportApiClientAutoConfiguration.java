package com.immx.industrialsupport.configuration;

import com.immx.industrialsupport.client.AuthenticationClient;
import com.immx.industrialsupport.client.IncidentClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@AutoConfiguration
@EnableConfigurationProperties(SupportApiProperties.class)
public class SupportApiClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "supportRestClient")
    public RestClient supportRestClient(RestClient.Builder builder,
                                        SupportApiProperties properties) {
        return builder.baseUrl(properties.baseUrl())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationClient.class)
    public AuthenticationClient authenticationClient() {
        return new AuthenticationClient();
    }

    @Bean
    @ConditionalOnMissingBean(IncidentClient.class)
    public IncidentClient incidentClient() {
        return new IncidentClient();
    }
}
