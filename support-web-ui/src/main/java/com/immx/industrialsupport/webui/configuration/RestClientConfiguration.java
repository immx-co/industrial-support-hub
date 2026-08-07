package com.immx.industrialsupport.webui.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {

    @Bean
    public RestClient supportRestClient(RestClient.Builder builder,
                                        SupportApiProperties properties) {
        return builder.baseUrl(properties.baseUrl())
                .build();
    }
}
