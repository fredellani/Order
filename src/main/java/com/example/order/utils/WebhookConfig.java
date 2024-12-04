package com.example.order.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "order.webhook")
public class WebhookConfig {
    private String address;
    private String clientId;
    private String secretId;

    @Bean
    public WebhookConfig webhook() {
        WebhookConfig webhook = new WebhookConfig();
        webhook.setAddress(address);
        webhook.setClientId(clientId);
        webhook.setSecretId(secretId);

        return webhook;
    }
}
