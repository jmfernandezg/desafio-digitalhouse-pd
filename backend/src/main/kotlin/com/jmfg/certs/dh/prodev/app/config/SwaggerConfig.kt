package com.jmfg.certs.dh.prodev.app.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun api(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Lodging and Customer API")
                    .description("API documentation for Lodging and Customer management")
                    .version("1.0.0")
            )
    }

    @Bean
    fun publicApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("public")
            .pathsToMatch("/v1/**")
            .build()
    }
}