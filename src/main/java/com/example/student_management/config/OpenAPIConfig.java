package com.example.student_management.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class OpenAPIConfig {
    private static final String SERVER_URL = "http://localhost:8080";
    private static final String TITLE = "Student Management System";
    private static final String DESCRIPTION = "Student management API documentation";
    private static final String CONTACT_EMAIL = "annguyenvan.2k@gmail.com";
    private static final String CONTACT_NAME = "An Nguyễn";
    private static final String VERSION = "1.0.0";
    private static final String SCHEME_NAME = "Authorization";
    private static final String BEARER_FORMAT = "JWT";
    private static final String SCHEME_TYPE = "bearer";
    private static final String SECURITY_REQUIREMENT_NAME = "JWT authentication";

    @Bean
    public OpenAPI OpenAPIConfiguration() {

        return new OpenAPI()
                .servers(List.of(new Server().url(SERVER_URL)))
                .info(infoConfiguration())
                .components(componentsConfiguration())
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_REQUIREMENT_NAME, List.of("read", "write")));
    }

    @Bean
    public Info infoConfiguration() {
        return new Info()
                .title(TITLE)
                .description(DESCRIPTION)
                .version(VERSION)
                .contact(new Contact()
                        .name(CONTACT_NAME)
                        .email(CONTACT_EMAIL));
    }

    @Bean
    public SecurityScheme securitySchemeConfigurationSecurityScheme() {
        return new SecurityScheme()
                .scheme(SCHEME_TYPE)
                .name(SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .bearerFormat(BEARER_FORMAT);
    }

    @Bean
    public Components componentsConfiguration() {
        return new Components()
                .addSecuritySchemes(SECURITY_REQUIREMENT_NAME, securitySchemeConfigurationSecurityScheme())
                .responses(globalResponses());
    }

    @Bean
    public Map<String, ApiResponse> globalResponses() {
        ApiResponse unauthorized = new ApiResponse().description("Unauthorized").content(new Content());
        ApiResponse methodNotAllowed = new ApiResponse().description("Method not allowed").content(new Content());
        ApiResponse resourceNotFound = new ApiResponse().description("Resource not found").content(new Content());
        Map<String, ApiResponse> apiResponseMap = new HashMap<>();
        apiResponseMap.put("unauthorized", unauthorized);
        apiResponseMap.put("methodNotAllowed", methodNotAllowed);
        apiResponseMap.put("resourceNotFound", resourceNotFound);
        return apiResponseMap;
    }
}

