package com.ceos19.everytime.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
    info = @Info(
        title = "Everytime API 명세서",
        version = "v1",
        description = "Everytime API 명세서"
    )
)
@Configuration
public class SwaggerConfig {
}
