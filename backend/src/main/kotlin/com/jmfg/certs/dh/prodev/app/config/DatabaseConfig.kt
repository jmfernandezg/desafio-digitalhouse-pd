package com.jmfg.certs.dh.prodev.app.config

import com.jmfg.certs.dh.prodev.app.repository.CustomerRepository
import com.jmfg.certs.dh.prodev.app.repository.LodgingRepository
import com.jmfg.certs.dh.prodev.app.repository.PhotoRepository
import com.jmfg.certs.dh.prodev.model.Category
import com.jmfg.certs.dh.prodev.model.Customer
import com.jmfg.certs.dh.prodev.model.Lodging
import com.jmfg.certs.dh.prodev.model.Photo
import net.datafaker.Faker
import org.slf4j.LoggerFactory.getLogger
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.random.Random

/**
 * Configuración de la base de datos y generación de datos de prueba
 *
 * Esta clase se encarga de la configuración de la base de datos y la población
 * de datos de prueba en el entorno de desarrollo. Genera automáticamente:
 * - Alojamientos con diferentes categorías
 * - Fotos para cada alojamiento
 * - Clientes con información básica
 *
 * La generación de datos utiliza rangos configurables para crear un conjunto
 * realista de información de prueba.
 */
@Configuration
@EntityScan("com.jmfg.certs.dh.prodev.model")
@EnableJpaRepositories("com.jmfg.certs.dh.prodev.app.repository")
class DatabaseConfig(
    private val lodgingRepository: LodgingRepository,
    private val customerRepository: CustomerRepository,
    private val photoRepository: PhotoRepository,
    /**
     * Número mínimo de ciudades por categoría de alojamiento
     */
    @Value("\${app.test-data.lodging.min-cities-per-category:5}")
    private val minCitiesPerCategory: Int,
    /**
     * Número máximo de ciudades por categoría de alojamiento
     */
    @Value("\${app.test-data.lodging.max-cities-per-category:10}")
    private val maxCitiesPerCategory: Int,
    /**
     * Número mínimo de alojamientos por ciudad
     */
    @Value("\${app.test-data.lodging.min-lodgings-per-city:10}")
    private val minLodgingsPerCity: Int,
    /**
     * Número máximo de alojamientos por ciudad
     */
    @Value("\${app.test-data.lodging.max-lodgings-per-city:15}")
    private val maxLodgingsPerCity: Int,
    /**
     * Número mínimo de fotos por alojamiento
     */
    @Value("\${app.test-data.lodging.min-photos-per-lodging:4}")
    private val minPhotosPerLodging: Int,
    /**
     * Número máximo de fotos por alojamiento
     */
    @Value("\${app.test-data.lodging.max-photos-per-lodging:8}")
    private val maxPhotosPerLodging: Int,
    /**
     * Número mínimo de clientes a generar
     */
    @Value("\${app.test-data.customer.min-customers:10}")
    private val minCustomers: Int,
    /**
     * Número máximo de clientes a generar
     */
    @Value("\${app.test-data.customer.max-customers:30}")
    private val maxCustomers: Int
) {
    private val logger = getLogger(DatabaseConfig::class.java)
    private val faker = Faker()

    /**
     * Genera los datos de prueba en la base de datos
     *
     * Este bean solo se activa en el perfil de desarrollo ('dev') y se ejecuta
     * al inicio de la aplicación para poblar la base de datos con información
     * de prueba.
     *
     * @param passwordEncoder Codificador de contraseñas para los clientes
     * @return CommandLineRunner que ejecuta la población de la base de datos
     */
    @Bean
    @Profile("dev")
    fun populateDatabase(passwordEncoder: PasswordEncoder): CommandLineRunner = CommandLineRunner {
        try {
            populateLodgings()
            populateCustomers(passwordEncoder)
        } catch (e: Exception) {
            logger.error("Error al poblar la base de datos", e)
            throw DatabasePopulationException("Error al poblar la base de datos", e)
        }
    }

    /**
     * Genera alojamientos para todas las categorías disponibles
     */
    private fun populateLodgings() {
        Category.entries.forEach { category ->
            logger.info("Generando alojamientos para la categoría ${category.name}")
            repeat(Random.nextInt(minCitiesPerCategory, maxCitiesPerCategory)) {
                val location = generateLocation()
                createLodgingsForLocation(category, location)
            }
        }
    }

    /**
     * Representa una ubicación con ciudad y país
     */
    private data class Location(val city: String, val country: String)

    /**
     * Genera una ubicación aleatoria
     */
    private fun generateLocation(): Location = Location(
        city = faker.address().city(),
        country = faker.address().country()
    )

    /**
     * Crea alojamientos para una ubicación y categoría específica
     */
    private fun createLodgingsForLocation(category: Category, location: Location) {
        repeat(Random.nextInt(minLodgingsPerCity, maxLodgingsPerCity)) {
            createLodging(category, location)
                .let { lodgingRepository.save(it) }
                .also { createPhotosForLodging(it) }
        }
    }

    /**
     * Crea un nuevo alojamiento con datos aleatorios
     */
    private fun createLodging(category: Category, location: Location): Lodging = Lodging(
        name = faker.company().name(),
        address = faker.address().fullAddress(),
        city = location.city,
        country = location.country,
        averageCustomerRating = generateRating(),
        stars = generateStars(),
        price = generatePrice(),
        description = faker.company().bs(),
        category = category,
        isFavorite = faker.bool().bool(),
        availableFrom = generateAvailableFrom(),
        availableTo = generateAvailableTo()
    )

    /**
     * Genera una calificación aleatoria entre 1 y 10
     */
    private fun generateRating(): Int = faker.number().numberBetween(1, 10)

    /**
     * Genera un número aleatorio de estrellas entre 1 y 5
     */
    private fun generateStars(): Int = faker.number().numberBetween(1, 5)

    /**
     * Genera un precio aleatorio entre 50 y 500 con dos decimales
     */
    private fun generatePrice(): Double = faker.number().randomDouble(2, 50, 500)

    /**
     * Genera una fecha de disponibilidad inicial
     */
    private fun generateAvailableFrom(): LocalDateTime =
        LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30).toLong())

    /**
     * Genera una fecha de disponibilidad final
     */
    private fun generateAvailableTo(): LocalDateTime =
        LocalDateTime.now().plusDays(faker.number().numberBetween(30, 60).toLong())

    /**
     * Crea fotos aleatorias para un alojamiento
     */
    private fun createPhotosForLodging(lodging: Lodging) {
        repeat(Random.nextInt(minPhotosPerLodging, maxPhotosPerLodging)) {
            Photo(
                url = generatePhotoUrl(),
                lodging = lodging
            ).run {
                photoRepository.save(this)
            }
        }
    }

    /**
     * Genera una URL aleatoria para una foto
     */
    private fun generatePhotoUrl(): String = faker.internet().image()

    /**
     * Genera clientes aleatorios
     */
    private fun populateCustomers(passwordEncoder: PasswordEncoder) {
        repeat(Random.nextInt(minCustomers, maxCustomers)) {
            createCustomer(passwordEncoder)
                .let { customerRepository.save(it) }
        }
    }

    /**
     * Crea un nuevo cliente con datos aleatorios
     */
    private fun createCustomer(passwordEncoder: PasswordEncoder): Customer {
        val username = faker.internet().username()
        return Customer(
            username = username,
            password = passwordEncoder.encode(username),
            firstName = faker.name().firstName(),
            lastName = faker.name().lastName(),
            dob = generateDateOfBirth(),
            email = faker.internet().emailAddress()
        )
    }

    /**
     * Genera una fecha de nacimiento aleatoria para clientes mayores de edad
     */
    private fun generateDateOfBirth(): LocalDate =
        LocalDate.now().minusYears(faker.number().numberBetween(18, 70).toLong())
}

/**
 * Excepción personalizada para errores durante la población de la base de datos
 *
 * @param message Mensaje descriptivo del error
 * @param cause Causa raíz de la excepción
 */
class DatabasePopulationException(message: String, cause: Throwable) : RuntimeException(message, cause)