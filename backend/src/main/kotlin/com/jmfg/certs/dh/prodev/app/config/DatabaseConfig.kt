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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
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
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun populateDatabase(
    ): CommandLineRunner {
        val faker = Faker()

        return CommandLineRunner {
            Category.entries.forEach { category ->
                repeat(10) {
                    Lodging(
                        name = faker.company().name(),
                        address = faker.address().fullAddress(),
                        rating = faker.number().randomDouble(1, 1, 5),
                        price = faker.number().randomDouble(2, 50, 500),
                        description = faker.siliconValley().invention(),
                        category = category,
                        availableFrom = LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30).toLong()),
                        availableTo = LocalDateTime.now().plusDays(faker.number().numberBetween(1, 30).toLong())
                    ).run {
                        lodgingRepository.save(this)
                    }.also { lo ->
                        repeat(3) {
                            Photo(
                                url = faker.internet().image(),
                                lodging = lo
                            ).run {
                                photoRepository.save(this)
                            }

                        }
                    }
                }
            }
            repeat(15) {
                val customer = Customer(
                    username = faker.internet().emailAddress(),
                    password = passwordEncoder().encode(it.toString()),
                    firstName = faker.name().firstName(),
                    lastName = faker.name().lastName(),
                    dob = LocalDate.now().minusYears(faker.number().numberBetween(18, 70).toLong()),
                    email = faker.internet().emailAddress()
                )
                customerRepository.save(customer)
            }

        }
    }
}