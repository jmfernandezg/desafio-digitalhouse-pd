package com.jmfg.certs.dh.prodev.app.service

import com.jmfg.certs.dh.prodev.app.repository.CustomerRepository
import com.jmfg.certs.dh.prodev.model.Customer
import com.jmfg.certs.dh.prodev.service.CustomerService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class CustomerServiceImpl(
    private val customerRepository: CustomerRepository,
    private val jwtEncoder: JwtEncoder
) : CustomerService {

    private val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()

    override fun login(username: String, password: String): String {
        val customer = customerRepository.findByUsernameAndPassword(username, password)
            ?: throw IllegalArgumentException("Invalid username or password")

        if (!passwordEncoder.matches(password, customer.password)) {
            throw IllegalArgumentException("Invalid username or password")
        }

        return generateToken(customer)
    }

    private fun generateToken(customer: Customer): String {
        val now = Instant.now()
        val expiry = now.plusSeconds(3600)

        val claims = JwtClaimsSet.builder()
            .subject(customer.username)
            .issuedAt(now)
            .expiresAt(expiry)
            .build()

        val encoderParameters = JwtEncoderParameters.from(claims)
        return jwtEncoder.encode(encoderParameters).tokenValue
    }
}
