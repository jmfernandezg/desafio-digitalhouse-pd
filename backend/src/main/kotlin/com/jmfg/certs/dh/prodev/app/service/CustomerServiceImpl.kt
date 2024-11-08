package com.jmfg.certs.dh.prodev.app.service

import com.jmfg.certs.dh.prodev.app.repository.CustomerRepository
import com.jmfg.certs.dh.prodev.model.Customer
import com.jmfg.certs.dh.prodev.model.dto.CustomerCreationRequest
import com.jmfg.certs.dh.prodev.model.dto.LoginRequest
import com.jmfg.certs.dh.prodev.service.CustomerService
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class CustomerServiceImpl(
    private val customerRepository: CustomerRepository,
    private val jwtEncoder: JwtEncoder,
    private val passwordEncoder: PasswordEncoder
) : CustomerService {

    private val logger = LoggerFactory.getLogger(CustomerService::class.java.name)

    override fun login(request: LoginRequest): String? =
        customerRepository.findByUsername(
            request.username
        )?.takeIf {
            logger.info("Customer found: $it")
            passwordEncoder.matches(request.password, it.password)
        }?.let {
            logger.info("Generating token for $it")
            generateToken(it)
        }

    override fun findAll(): List<Customer> = customerRepository.findAll()

    override fun create(request: CustomerCreationRequest): Customer = Customer(
        username = request.username,
        password = passwordEncoder.encode(request.password),
        email = request.email,
        firstName = request.firstName,
        lastName = request.lastName
    ).run {
        customerRepository.save(this)
    }

    override fun delete(id: String) = customerRepository.deleteById(id)

    override fun update(customer: Customer): Customer = customerRepository.save(customer)

    private fun generateToken(customer: Customer) =
        jwtEncoder.encode(
            JwtEncoderParameters.from(
                JwtClaimsSet.builder()
                    .subject(customer.username)
                    .issuedAt(Instant.now())
                    .expiresAt(Instant.now().plusSeconds(3600))
                    .build()
            )
        ).tokenValue
}
