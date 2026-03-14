package com.rev.api_gateway.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;   // ✅ correct
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.models.GroupedOpenApi;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi userServiceApi() {
        return GroupedOpenApi.builder()
                .group("user-service")
                .pathsToMatch("/api/users/**")
                .build();
    }

    @Bean
    public GroupedOpenApi passwordServiceApi() {
        return GroupedOpenApi.builder()
                .group("password-service")
                .pathsToMatch("/api/passwords/**")
                .build();
    }

    @Bean
    public OpenAPI gatewayOpenAPI() {

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("BearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"));
    }
}