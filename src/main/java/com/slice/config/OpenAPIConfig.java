package com.slice.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Boot 3.x Swagger API 文档")
                        .description("SpringBoot3 集成 Swagger3.0 API 文档")
                        .version("v1"))
                .externalDocs(new ExternalDocumentation()
                        .description("API 文档地址")
                        .url("/"));
    }
}
