package com.jmfg.certs.dh.prodev.app.service

import com.jmfg.certs.dh.prodev.app.repository.CustomerRepository
import com.jmfg.certs.dh.prodev.model.Customer
import com.jmfg.certs.dh.prodev.model.dto.*
import com.jmfg.certs.dh.prodev.model.toCustomerItem
import com.jmfg.certs.dh.prodev.service.CustomerService
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Service
class CustomerServiceImpl(
    private val customerRepository: CustomerRepository,
    private val jwtEncoder: JwtEncoder,
    private val passwordEncoder: PasswordEncoder
) : CustomerService {

    private val logger = LoggerFactory.getLogger(CustomerServiceImpl::class.java)

    @Transactional(readOnly = true)
    override suspend fun login(request: LoginRequest): CustomerResponse.CustomerItem? {
        logger.debug("Intento de inicio de sesión para: ${request.email}")
        return customerRepository.run {
            findByEmail(request.email)
                ?.let { customer ->
                    if (passwordEncoder.matches(request.password, customer.password)) {
                        logger.info("Inicio de sesión exitoso para: ${request.email}")
                        return customer.toCustomerItem().copy(token = generateToken(customer))
                    }
                    logger.warn("Contraseña incorrecta para: ${request.email}")
                    throw IllegalArgumentException("Credenciales inválidas")
                }
        }
    }

    @Transactional(readOnly = true)
    override suspend fun findAll(): CustomerResponse {
        logger.debug("Consultando todos los clientes")
        return CustomerResponse(customerRepository.findAll().map { it.toCustomerItem() })
    }

    @Transactional
    override suspend fun create(request: CustomerCreationRequest): CustomerResponse.CustomerItem {
        logger.debug("Creando nuevo cliente: ${request.email}")

        validateRequest(request)

        return Customer(
            email = request.email,
            password = passwordEncoder.encode(request.password),
            firstName = request.firstName,
            lastName = request.lastName,
            dob = request.dob,
            passportNumber = request.passportNumber,
            passportExpiry = request.passportExpiry,
            phoneNumber = request.phoneNumber,
            countryOfResidence = request.countryOfResidence,
            preferredFrequentFlyerProgram = request.preferredFrequentFlyerProgram
        ).run {
            customerRepository.save(this).toCustomerItem()
        }.also {
            logger.info("Cliente creado exitosamente: ${request.email}")
        }
    }

    @Transactional
    override suspend fun delete(id: Long) {
        logger.debug("Eliminando cliente con ID: $id")
        customerRepository.run {
            if (!existsById(id)) {
                logger.error("Intento de eliminar cliente inexistente: $id")
                throw NoSuchElementException("Cliente no encontrado con ID: $id")
            }
            deleteById(id)
            logger.info("Cliente eliminado exitosamente: $id")
        }
    }

    @Transactional
    override suspend fun update(id: Long, request: CustomerUpdateRequest): CustomerResponse.CustomerItem {
        logger.debug("Actualizando cliente con ID: $id")
        return customerRepository.findById(id).map { customer ->
            val updatedCustomer = customer.copy(
                firstName = request.firstName ?: customer.firstName,
                lastName = request.lastName ?: customer.lastName,
                passportNumber = request.passportNumber ?: customer.passportNumber,
                passportExpiry = request.passportExpiry ?: customer.passportExpiry,
                phoneNumber = request.phoneNumber ?: customer.phoneNumber,
                countryOfResidence = request.countryOfResidence ?: customer.countryOfResidence,
                preferredFrequentFlyerProgram = request.preferredFrequentFlyerProgram
                    ?: customer.preferredFrequentFlyerProgram
            )
            customerRepository.save(updatedCustomer).toCustomerItem()
        }.orElseThrow {
            logger.error("Intento de actualizar cliente inexistente: $id")
            NoSuchElementException("Cliente no encontrado con ID: $id")
        }
    }

    /**
     * Genera un token JWT para el cliente
     */
    private fun generateToken(customer: Customer): String {
        val claims = JwtClaimsSet.builder()
            .id(customer.id.toString())
            .subject(customer.email) // Ahora usamos email como identificador principal
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
            .claim("email", customer.email)
            .claim("nombre", customer.fullName)
            .build()

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).tokenValue
    }

    // ... existing methods remain the same ...

    @Transactional(readOnly = true)
    override suspend fun findByCountry(country: String): CustomerResponse {
        logger.debug("Buscando clientes en el país: $country")
        return customerRepository.findByCountryOfResidence(country)
            .map { it.toCustomerItem() }
            .let { CustomerResponse(it) }
            .also { logger.debug("Encontrados ${it.customers.size} clientes en $country") }
    }

    @Transactional(readOnly = true)
    override suspend fun findByPassportExpiryBefore(date: LocalDate): CustomerResponse {
        logger.debug("Buscando clientes con pasaportes que expiran antes de: $date")
        return customerRepository.findByPassportExpiryBefore(date)
            .map { it.toCustomerItem() }
            .let { CustomerResponse(it) }
            .also { logger.debug("Encontrados ${it.customers.size} clientes con pasaportes por vencer") }
    }

    @Transactional(readOnly = true)
    override suspend fun getStatistics(): CustomerStatistics {
        logger.debug("Generando estadísticas de clientes")

        val allCustomers = customerRepository.findAll()
        val today = LocalDate.now()

        return CustomerStatistics(
            totalCustomers = allCustomers.size,
            customersByCountry = allCustomers
                .groupBy { it.countryOfResidence ?: "No especificado" }
                .mapValues { it.value.size },
            averageAge = allCustomers
                .map { it.age }
                .average()
                .takeIf { !it.isNaN() } ?: 0.0,
            expiringPassportsCount = allCustomers.count { customer ->
                customer.passportExpiry?.let { expiry ->
                    expiry.isAfter(today) && expiry.isBefore(today.plusMonths(6))
                } ?: false
            },
            customersWithoutPassport = allCustomers.count {
                it.passportNumber.isNullOrBlank() || it.passportExpiry == null
            }
        ).also {
            logger.info(
                """
                Estadísticas generadas:
                - Total clientes: ${it.totalCustomers}
                - Países representados: ${it.customersByCountry.size}
                - Edad promedio: ${String.format("%.1f", it.averageAge)}
                - Pasaportes por vencer: ${it.expiringPassportsCount}
                - Sin pasaporte: ${it.customersWithoutPassport}
            """.trimIndent()
            )
        }
    }

    /**
     * Extensión del método existente validateRequest para incluir validaciones adicionales
     */
    private fun validateRequest(request: CustomerCreationRequest) {
        if (customerRepository.existsByEmail(request.email)) {
            logger.warn("Intento de crear cliente con email existente: ${request.email}")
            throw IllegalArgumentException("El correo electrónico ya está registrado")
        }

        if (request.password.length < 8) {
            logger.warn("Intento de crear cliente con contraseña débil")
            throw IllegalArgumentException("La contraseña debe tener al menos 8 caracteres")
        }

        request.passportExpiry?.let { expiry ->
            if (expiry.isBefore(LocalDate.now())) {
                logger.warn("Intento de crear cliente con pasaporte vencido")
                throw IllegalArgumentException("La fecha de vencimiento del pasaporte no puede ser pasada")
            }
        }

        request.phoneNumber?.let { phone ->
            if (!phone.matches(Regex("^\\+?[1-9]\\d{1,14}$"))) {
                logger.warn("Intento de crear cliente con número de teléfono inválido")
                throw IllegalArgumentException("El formato del número de teléfono es inválido")
            }
        }
    }
}