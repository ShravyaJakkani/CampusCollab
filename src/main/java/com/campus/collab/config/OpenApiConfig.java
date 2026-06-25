package com.campus.collab.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI campusCollabOpenAPI() {

        Contact contact = new Contact();
        contact.setEmail("info@campuscollab.com");
        contact.setName("Campus Collab Team");

        License license = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Campus Collaboration Platform API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints for managing posts, comments, and likes in the Campus Collaboration Platform.")
                .license(license);

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Provide the JWT token obtained from /api/auth/login endpoint");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("Bearer Authentication");

        return new OpenAPI()
        .info(info)
        .addSecurityItem(securityRequirement)
        .components(new io.swagger.v3.oas.models.Components()
                .addSecuritySchemes("Bearer Authentication", securityScheme));
    }
}
