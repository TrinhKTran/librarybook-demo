package com.library.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${openapi.service.title}")
    private String serviceTitle;

    @Value("${openapi.service.version}")
    private String serviceVersion;

    @Value("${openapi.service.url}")
    private String url;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(getInfo())
                .servers(getServers())
                .components(getComponents())
                .addSecurityItem(getSecurityRequirement());
    }

    private Info getInfo() {
        return new Info()
                .title(serviceTitle)
                .version(serviceVersion)
                .description("API documentation for " + serviceTitle)
                .contact(getContact())
                .license(getLicense());
    }

    private Contact getContact() {
        return new Contact()
                .name("Health Care Development Team")
                .email("devteam@example.com")
                .url("https://example.com");
    }

    private License getLicense() {
        return new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");
    }

    private List<Server> getServers() {
        Server server = new Server();
        server.setUrl(url);
        server.setDescription("Development server");
        return List.of(server);
    }

    private Components getComponents() {
        return new Components()
                .addSecuritySchemes("bearerAuth", getBearerAuthScheme())
                .addSecuritySchemes("basicAuth", getBasicAuthScheme());
    }

    private SecurityScheme getBearerAuthScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("JWT Bearer token authentication");
    }

    private SecurityScheme getBasicAuthScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("basic")
                .description("Basic HTTP authentication");
    }

    private SecurityRequirement getSecurityRequirement() {
        return new SecurityRequirement()
                .addList("bearerAuth");
    }
}