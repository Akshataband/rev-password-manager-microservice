package com.rev.security_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI securityServiceOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Security Service API")
                        .description("Handles password security operations like strength check, encryption and decryption")
                        .version("1.0"));
    }
}