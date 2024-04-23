package com.ceos19.everytime.config;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;

@OpenAPIDefinition(
    info = @Info(
        title = "Everytime API 명세서",
        version = "v1",
        description = "Everytime API 명세서"
    )
)
@Configuration
public class SwaggerConfig {

    @Bean
    public OperationCustomizer addCustomHeader() {
        return (operation, handlerMethod) -> {
            Parameter headerParameter = new io.swagger.v3.oas.models.parameters.Parameter()
                    .name("access") // 헤더의 이름을 "access"로 지정
                    .description("Access token") // 헤더의 설명 추가
                    .required(false) // 선택 사항으로 설정
                    .schema(new io.swagger.v3.oas.models.media.StringSchema())
                    .in("header"); // 헤더로 설정

            operation.addParametersItem(headerParameter);
            return operation;
        };
    }
}
