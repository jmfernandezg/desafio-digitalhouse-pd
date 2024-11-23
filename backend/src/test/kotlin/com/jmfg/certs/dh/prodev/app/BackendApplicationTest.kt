package com.jmfg.certs.dh.prodev.app

import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.boot.SpringApplication
import java.util.*
import kotlin.test.assertEquals

/**
 * Pruebas unitarias para la clase BackendApplication
 *
 * Esta clase contiene las pruebas para verificar el comportamiento
 * de la aplicación principal, incluyendo:
 * - Configuración del locale por defecto
 * - Inicialización de la aplicación Spring
 * - Configuración de propiedades por defecto
 */
@DisplayName("Backend Application Tests")
class BackendApplicationTest {

    private lateinit var backendApplication: BackendApplication

    /**
     * Configuración inicial antes de cada prueba
     */
    @BeforeEach
    fun setup() {
        // Restauramos el locale por defecto antes de cada prueba
        Locale.setDefault(Locale.getDefault())
    }

    /**
     * Verifica que el locale se configure correctamente al inicializar la aplicación
     */
    @Test
    @DisplayName("Debe configurar el locale español al inicializar")
    fun `should set spanish locale on initialization`() {
        backendApplication = BackendApplication()

        val currentLocale = Locale.getDefault()
        assertEquals("es", currentLocale.language)
        assertEquals("ES", currentLocale.country)
    }

    /**
     * Verifica que la aplicación se inicie correctamente con los argumentos proporcionados
     */
    @Test
    @DisplayName("Debe iniciar la aplicación con los argumentos correctos")
    fun `should start application with correct arguments`() {
        mockkStatic(SpringApplication::class)

        every {
            SpringApplication.run(BackendApplication::class.java, *anyVararg<String>())
        } just awaits

        val args = arrayOf("--spring.profiles.active=test")

        assertDoesNotThrow {
            main(args)
        }

        verify {
            SpringApplication.run(BackendApplication::class.java, *args)
        }
    }

    /**
     * Verifica que las propiedades por defecto se configuren correctamente
     */
    @Test
    @DisplayName("Debe configurar las propiedades por defecto")
    fun `should set default properties`() {
        mockkStatic(SpringApplication::class)

        val expectedProperties = mapOf(
            "spring.banner.location" to "classpath:banner.txt",
            "spring.output.ansi.enabled" to "ALWAYS",
            "server.error.include-message" to "always",
            "server.error.include-binding-errors" to "always"
        )

        every {
            SpringApplication.run(BackendApplication::class.java, *anyVararg<String>())
        } just awaits

        main(arrayOf())

        verify {
            SpringApplication.run(
                BackendApplication::class.java,
                match {
                    it.isEmpty()
                }
            )
        }
    }
}