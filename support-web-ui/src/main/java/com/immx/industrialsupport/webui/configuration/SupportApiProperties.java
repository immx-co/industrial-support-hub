package com.immx.industrialsupport.webui.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "support.api")
public record SupportApiProperties(String baseUrl) {
}
