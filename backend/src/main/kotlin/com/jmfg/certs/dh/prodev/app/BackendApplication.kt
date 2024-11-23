package com.jmfg.certs.dh.prodev.app

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License
import io.swagger.v3.oas.annotations.servers.Server
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import java.util.*

/**
 * Aplicación principal del backend de gestión de alojamientos turísticos
 *
 * Esta clase es el punto de entrada principal de la aplicación Spring Boot.
 * Configura la documentación OpenAPI, habilita la programación de tareas
 * y establece las propiedades básicas de la aplicación.
 *
 * Características principales:
 * - Documentación API con OpenAPI/Swagger
 * - Soporte para programación de tareas
 * - Escaneo automático de propiedades de configuración
 * - Múltiples perfiles de ejecución (dev, prod)
 */
@SpringBootApplication
@EnableScheduling
@ConfigurationPropertiesScan
@OpenAPIDefinition(
    info = Info(
        title = "API de Gestión de Alojamientos Turísticos",
        version = "1.0.0",
        description = """
            API Backend para la gestión de alojamientos turísticos.
            
            Funcionalidades principales:
            - Gestión de alojamientos
            - Reservas y disponibilidad
            - Gestión de clientes
            - Categorización y búsqueda
            - Reportes y estadísticas
            
            Para más información, consulte la documentación completa.
        """,
        contact = Contact(
            name = "José Fernández",
            email = "jmfernandezg.awe@gmail.com",
            url = "https://empresa.com"
        ),
        license = License(
            name = "Apache 2.0",
            url = "https://www.apache.org/licenses/LICENSE-2.0"
        )
    ),
    servers = [
        Server(
            url = "http://localhost:8080",
            description = "Servidor de desarrollo"
        ),
        Server(
            url = "https://api.empresa.com",
            description = "Servidor de producción"
        )
    ],
    tags = [
        Tag(
            name = "Alojamientos",
            description = "Operaciones relacionadas con la gestión de alojamientos"
        ),
        Tag(
            name = "Reservas",
            description = "Operaciones relacionadas con reservas y disponibilidad"
        ),
        Tag(
            name = "Clientes",
            description = "Operaciones relacionadas con la gestión de clientes"
        ),
        Tag(
            name = "Categorías",
            description = "Operaciones relacionadas con categorías de alojamientos"
        )
    ]
)
class BackendApplication {

    /**
     * Configuración inicial de la aplicación
     */
    init {
        // Configuración del locale para formateo consistente
        Locale.setDefault(Locale("es", "ES"))
    }

}

/**
 * Punto de entrada principal de la aplicación
 *
 * @param args Argumentos de línea de comandos
 */
fun main(args: Array<String>) {
    runApplication<BackendApplication>(*args) {
        // Configuraciones adicionales de la aplicación
        setDefaultProperties(
            mapOf(
                "spring.banner.location" to "classpath:banner.txt",
                "spring.output.ansi.enabled" to "ALWAYS",
                "server.error.include-message" to "always",
                "server.error.include-binding-errors" to "always"
            )
        )
    }
}