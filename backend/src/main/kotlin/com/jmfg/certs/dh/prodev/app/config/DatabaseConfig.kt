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
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDate
import java.time.LocalDateTime

@Configuration
class DatabaseConfig(
    private val lodgingRepository: LodgingRepository,
    private val customerRepository: CustomerRepository,
    private val photoRepository: PhotoRepository
) {
    private val logger = getLogger(DatabaseConfig::class.java)

    @Bean
    @Profile("dev")
    fun populateDatabase(passwordEncoder: PasswordEncoder): CommandLineRunner {
        return CommandLineRunner {
            val faker = Faker()
            populateLodgings(faker)
            populateCustomers(faker, passwordEncoder)
        }
    }

    private fun populateLodgings(faker: Faker) {
        Category.entries.forEach { category ->
            logger.info("Populating lodgings for category ${category.name}")
            repeat(10) {
                Lodging(
                    name = faker.company().name(),
                    address = faker.address().fullAddress(),
                    rating = faker.number().randomDouble(1, 5, 5),
                    price = faker.number().randomDouble(2, 50, 500),
                    description = faker.company().bs(),
                    category = category,
                    availableFrom = LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30).toLong()),
                    availableTo = LocalDateTime.now().plusDays(faker.number().numberBetween(1, 30).toLong())
                ).run {
                    lodgingRepository.save(this)
                }.also { lodging ->
                    logger.info("Populating photos for lodging ${lodging.name}")
                    repeat(3) {
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
        repeat(15) {
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
                logger.info("Populated customer $username")
            }
        }
    }
}