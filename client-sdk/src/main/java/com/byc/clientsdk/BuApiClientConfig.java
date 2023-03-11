package com.byc.clientsdk;

import com.byc.clientsdk.client.BuClient;
import lombok.Data;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("buapi.client")
@Data
@ComponentScan
public class BuApiClientConfig {
    private String accessKey;

    private String secretKey;

    @Bean
    public BuClient buClient() {
        return new BuClient(accessKey, secretKey);
    }
}
