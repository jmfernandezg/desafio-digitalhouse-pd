package com.jmfg.certs.dh.prodev.app.config

import com.jmfg.certs.dh.prodev.app.repository.CustomerRepository
import com.jmfg.certs.dh.prodev.app.repository.LodgingRepository
import com.jmfg.certs.dh.prodev.app.repository.PhotoRepository
import com.jmfg.certs.dh.prodev.model.Category
import com.jmfg.certs.dh.prodev.model.Customer
import com.jmfg.certs.dh.prodev.model.Lodging
import com.jmfg.certs.dh.prodev.model.Photo
import net.datafaker.Faker
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

    @Bean
    @Profile("dev")
    fun populateDatabase(
        passwordEncoder: PasswordEncoder
    ): CommandLineRunner {
        return CommandLineRunner {
            val faker = Faker()
            Category.entries.forEach {
                repeat(10) { index ->
                    Lodging(
                        id = index.toString(),
                        name = faker.company().name(),
                        address = faker.address().fullAddress(),
                        rating = faker.number().randomDouble(1, index, 5),
                        price = faker.number().randomDouble(2, 50, 500),
                        description = faker.company().bs(),
                        category = it,
                        availableFrom = LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30).toLong()),
                        availableTo = LocalDateTime.now().plusDays(faker.number().numberBetween(1, 30).toLong())
                    ).run {
                        lodgingRepository.save(this)
                    }.also {
                        repeat(3) { index ->
                            Photo(
                                id = index.toString(),
                                url = faker.internet().image(),
                                lodging = it
                            ).run {
                                photoRepository.save(this)
                            }
                        }
                    }
                }
            }
            repeat(15) { ix ->
                val username = faker.internet().username()
                Customer(
                    id = ix.toString(),
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
}