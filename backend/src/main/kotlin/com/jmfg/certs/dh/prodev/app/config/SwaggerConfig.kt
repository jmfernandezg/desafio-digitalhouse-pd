package com.jmfg.certs.dh.prodev.app.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configuración de Swagger/OpenAPI para la documentación de la API
 *
 * Esta clase proporciona la configuración necesaria para generar la documentación
 * interactiva de la API utilizando OpenAPI 3.0. Define múltiples grupos de endpoints,
 * información general de la API, y configuración de seguridad.
 */
@Configuration
class SwaggerConfig(
    @Value("\${appap.version:1.0.0}") private val appVersion: String,
    @Value("\${app.name:API de Alojamientos}") private val appName: String
) {

    /**
     * Configura la información principal de OpenAPI
     *
     * @return Configuración principal de OpenAPI con toda la información del servicio
     */
    @Bean
    fun api(): OpenAPI = OpenAPI()
        .info(
            Info()
                .title("$appName - Documentación API")
                .version(appVersion)
                .description(
                    """
                    API REST para la gestión de alojamientos turísticos y reservas.
                    
                    Características principales:
                    * Gestión completa de alojamientos
                    * Sistema de reservas
                    * Administración de clientes
                    * Categorización y búsqueda
                    * Gestión de disponibilidad
                    
                    Para más información, consulte nuestra documentación detallada.
                """.trimIndent()
                )
                .contact(
                    Contact()
                        .name("Jose Fernandez")
                        .email("jmfernandezg.awe gmail.com")
                        .url("https://empresa.com")
                )
                .license(
                    License()
                        .name("Apache 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0")
                )
        )
        .servers(
            listOf(
                Server()
                    .url("http://localhost:8080")
                    .description("Servidor de desarrollo")

            )
        )
        .components(
            Components()
                .addSecuritySchemes(
                    "bearerAuth",
                    SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("JWT token de autenticación")
                )
        )
        .addSecurityItem(
            SecurityRequirement().addList("bearerAuth")
        )

    /**
     * Define el grupo de API pública
     *
     * Agrupa todos los endpoints públicos que no requieren autenticación
     *
     * @return Configuración del grupo de API pública
     */
    @Bean
    fun publicApi(): GroupedOpenApi = GroupedOpenApi.builder()
        .group("api-publica")
        .pathsToMatch("/v1/public/**")
        .addOpenApiCustomizer { openApi ->
            openApi.info.description = "APIs públicas que no requieren autenticación"
        }
        .build()

    /**
     * Define el grupo de API privada
     *
     * Agrupa todos los endpoints que requieren autenticación
     *
     * @return Configuración del grupo de API privada
     */
    @Bean
    fun privateApi(): GroupedOpenApi = GroupedOpenApi.builder()
        .group("api-privada")
        .pathsToMatch("/v1/lodging/**", "/v1/reservation/**", "/v1/customer/**")
        .addOpenApiCustomizer { openApi ->
            openApi.info.description = "APIs privadas que requieren autenticación JWT"
        }
        .build()

    /**
     * Define el grupo de API administrativa
     *
     * Agrupa todos los endpoints administrativos
     *
     * @return Configuración del grupo de API administrativa
     */
    @Bean
    fun adminApi(): GroupedOpenApi = GroupedOpenApi.builder()
        .group("api-admin")
        .pathsToMatch("/v1/admin/**")
        .addOpenApiCustomizer { openApi ->
            openApi.info.description = "APIs administrativas con acceso restringido"
        }
        .build()
}