package com.jmfg.certs.dh.prodev.app.config

import com.jmfg.certs.dh.prodev.app.repository.CustomerRepository
import com.jmfg.certs.dh.prodev.app.repository.LodgingRepository
import com.jmfg.certs.dh.prodev.app.repository.PhotoRepository
import com.jmfg.certs.dh.prodev.model.Category
import com.jmfg.certs.dh.prodev.model.Customer
import com.jmfg.certs.dh.prodev.model.Lodging
import com.jmfg.certs.dh.prodev.model.Photo
import com.nimbusds.jose.jwk.source.ImmutableSecret
import net.datafaker.Faker
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.crypto.spec.SecretKeySpec

@Configuration
@EntityScan("com.jmfg.certs.dh.prodev.model")
@EnableJpaRepositories("com.jmfg.certs.dh.prodev.app.repository")
class BackendConfig {

    @Value("\${backend.jwt}")
    private val jwtSecret: String? = null

    @Bean
    fun jwtEncoder(): JwtEncoder = NimbusJwtEncoder(
        ImmutableSecret(
            SecretKeySpec(
                Base64.getDecoder().decode(jwtSecret), "HmacSHA256"
            )
        )
    )

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    @Transactional
    fun populateDatabase(lodgingRepository: LodgingRepository, customerRepository: CustomerRepository, photoRepository: PhotoRepository): String {
        val faker = Faker()

        Category.entries.forEach { category ->
            repeat(10) {
                Lodging(
                    name = faker.company().name(),
                    address = faker.address().fullAddress(),
                    rating = faker.number().randomDouble(1, 1, 5),
                    price = faker.number().randomDouble(2, 50, 500),
                    description = faker.lorem().paragraph(),
                    category = category,
                    availableFrom = LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30).toLong()),
                    availableTo = LocalDateTime.now().plusDays(faker.number().numberBetween(1, 30).toLong())
                ).run {  
                    lodgingRepository.save(this)
                 }.also {  lo ->
                    repeat(3) {
                        Photo(
                            url = faker.internet().image(),
                            lodging = lo
                        ).run { photoRepository.save(this)
                    }

                  }
            }
        }
    }
        repeat(15) {
            val password = faker.internet().password()
            val customer = Customer(
                username = faker.internet().emailAddress(),
                password = passwordEncoder().encode(password),
                firstName = faker.name().firstName(),
                lastName = faker.name().lastName(),
                dob = LocalDate.now().minusYears(faker.number().numberBetween(18, 70).toLong()),
                email = faker.internet().emailAddress()
            )
            customerRepository.save(customer)
        }

        return "Database populated"
    }

}