package com.example.student_management.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    private static final String SCHEME_NAME = "JWT authentication";
    private static final String SCHEME_TYPE = "bearer";

    @Bean
    public OpenAPI OpenAPIConfig() {
        OpenAPI openAPI = new OpenAPI();
        // define server
        Server server = new Server();
        server.setUrl(SERVER_URL);
        // define info
        Info info = new Info();
        info.setTitle(TITLE);
        info.setDescription(DESCRIPTION);
        info.setVersion(VERSION);

        Contact contact = new Contact();
        contact.setEmail(CONTACT_EMAIL);
        contact.setName(CONTACT_NAME);

        info.setContact(contact);
        info.setVersion(VERSION);
        // define security scheme
        SecurityScheme securityScheme = new SecurityScheme();
        securityScheme.setType(SecurityScheme.Type.HTTP);
        securityScheme.setIn(SecurityScheme.In.HEADER);
        securityScheme.setScheme(SCHEME_TYPE);
        securityScheme.setBearerFormat("JWT");
        securityScheme.setName("Authorization");
        Map<String, SecurityScheme> securitySchemeMap = new HashMap<>();
        securitySchemeMap.put(SCHEME_NAME, securityScheme);


        openAPI.setInfo(info);
        openAPI.setServers(List.of(server));
        openAPI.setComponents(new Components().securitySchemes(securitySchemeMap));
        return openAPI;

    }
}
