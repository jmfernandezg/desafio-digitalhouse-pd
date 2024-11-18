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

@Configuration
@EntityScan("com.jmfg.certs.dh.prodev.model")
@EnableJpaRepositories("com.jmfg.certs.dh.prodev.app.repository")
class DatabaseConfig(
    private val lodgingRepository: LodgingRepository,
    private val customerRepository: CustomerRepository,
    private val photoRepository: PhotoRepository
) {
    private val logger = getLogger(DatabaseConfig::class.java)

    @Bean
    @Profile("dev")
    fun populateDatabase(passwordEncoder: PasswordEncoder): CommandLineRunner = CommandLineRunner {
        val faker = Faker()
        populateLodgings(faker)
        populateCustomers(faker, passwordEncoder)
    }

    private fun populateLodgings(faker: Faker) {
        Category.entries.forEach { category ->
            logger.info("Populating lodgings for category ${category.name}")
            repeat(Random.nextInt(50, 100)) {
                Lodging(
                    name = faker.company().name(),
                    address = faker.address().fullAddress(),
                    city = faker.address().city(),
                    averageCustomerRating = faker.number().numberBetween(1, 10),
                    stars = faker.number().numberBetween(1, 5),
                    price = faker.number().randomDouble(2, 50, 500),
                    description = faker.company().bs(),
                    category = category,
                    isFavorite = faker.bool().bool(),
                    availableFrom = LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30).toLong()),
                    availableTo = LocalDateTime.now().plusDays(faker.number().numberBetween(1, 30).toLong())
                ).run {
                    lodgingRepository.save(this)
                }.also { lodging ->
                    repeat(Random.nextInt(4, 8)) {
                        Photo(
                            url = faker.internet().image(),
                            lodging = lodging
                        ).run {
                            photoRepository.save(this)
                        }
                    }
                }
            }
        }
    }

    private fun populateCustomers(faker: Faker, passwordEncoder: PasswordEncoder) {
        repeat(Random.nextInt(200, 300)) {
            val username = faker.internet().username()
            Customer(
                username = username,
                password = passwordEncoder.encode(username),
                firstName = faker.name().firstName(),
                lastName = faker.name().lastName(),
                dob = LocalDate.now().minusYears(faker.number().numberBetween(18, 70).toLong()),
                email = faker.internet().emailAddress()
            ).run {
                customerRepository.save(this)
            }
        }
    }
}