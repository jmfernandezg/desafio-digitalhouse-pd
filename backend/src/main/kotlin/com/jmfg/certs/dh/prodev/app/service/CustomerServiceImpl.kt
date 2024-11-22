package com.jmfg.certs.dh.prodev.app.service

import com.jmfg.certs.dh.prodev.app.repository.CustomerRepository
import com.jmfg.certs.dh.prodev.model.Customer
import com.jmfg.certs.dh.prodev.model.dto.CustomerCreationRequest
import com.jmfg.certs.dh.prodev.model.dto.CustomerResponse
import com.jmfg.certs.dh.prodev.model.dto.LoginRequest
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
import java.time.temporal.ChronoUnit

/**
 * Implementación del servicio de gestión de clientes
 *
 * Esta clase implementa la lógica de negocio para todas las operaciones
 * relacionadas con clientes, incluyendo:
 * - Autenticación y generación de tokens
 * - Gestión de datos de cliente
 * - Operaciones CRUD básicas
 */
@Service
class CustomerServiceImpl(
    private val customerRepository: CustomerRepository,
    private val jwtEncoder: JwtEncoder,
    private val passwordEncoder: PasswordEncoder
) : CustomerService {

    private val logger = LoggerFactory.getLogger(CustomerServiceImpl::class.java)

    /**
     * Realiza el inicio de sesión del cliente
     *
     * @param request Solicitud de login con credenciales
     * @return Información del cliente con token JWT si las credenciales son válidas
     */
    @Transactional(readOnly = true)
    override suspend fun login(request: LoginRequest): CustomerResponse.CustomerItem? {
        logger.debug("Intento de inicio de sesión para usuario: ${request.username}")

        return customerRepository.findByUsername(request.username)?.let { customer ->
            if (passwordEncoder.matches(request.password, customer.password)) {
                logger.info("Inicio de sesión exitoso para usuario: ${request.username}")
                customer.toCustomerItem().copy(token = generarToken(customer))
            } else {
                logger.warn("Contraseña incorrecta para usuario: ${request.username}")
                null
            }
        }.also {
            if (it == null) logger.warn("Usuario no encontrado: ${request.username}")
        }
    }

    /**
     * Obtiene todos los clientes registrados
     *
     * @return Lista de todos los clientes en el sistema
     */
    @Transactional(readOnly = true)
    override suspend fun findAll(): CustomerResponse {
        logger.debug("Consultando todos los clientes")
        return CustomerResponse(customerRepository.findAll().map { it.toCustomerItem() })
    }

    /**
     * Crea un nuevo cliente en el sistema
     *
     * @param request Datos del nuevo cliente
     * @return Información del cliente creado
     * @throws IllegalArgumentException si el nombre de usuario o email ya existen
     */
    @Transactional
    override suspend fun create(request: CustomerCreationRequest): CustomerResponse.CustomerItem {
        logger.debug("Creando nuevo cliente: ${request.username}")

        validarDatosCliente(request)

        return Customer(
            username = request.username,
            password = passwordEncoder.encode(request.password),
            email = request.email,
            firstName = request.firstName,
            lastName = request.lastName
        ).run {
            customerRepository.save(this).toCustomerItem()
        }.also {
            logger.info("Cliente creado exitosamente: ${request.username}")
        }
    }

    /**
     * Elimina un cliente del sistema
     *
     * @param id Identificador del cliente a eliminar
     * @throws NoSuchElementException si el cliente no existe
     */
    @Transactional
    override suspend fun delete(id: String) {
        logger.debug("Eliminando cliente con ID: $id")

        if (!customerRepository.existsById(id)) {
            logger.error("Intento de eliminar cliente inexistente: $id")
            throw NoSuchElementException("Cliente no encontrado con ID: $id")
        }

        customerRepository.deleteById(id)
        logger.info("Cliente eliminado exitosamente: $id")
    }

    /**
     * Actualiza los datos de un cliente existente
     *
     * @param customer Nuevos datos del cliente
     * @return Cliente actualizado
     * @throws NoSuchElementException si el cliente no existe
     */
    @Transactional
    override suspend fun update(customer: Customer): CustomerResponse.CustomerItem {
        logger.debug("Actualizando cliente con ID: ${customer.id}")

        if (!customerRepository.existsById(customer.id)) {
            logger.error("Intento de actualizar cliente inexistente: ${customer.id}")
            throw NoSuchElementException("Cliente no encontrado con ID: ${customer.id}")
        }

        return customerRepository.save(customer)
            .toCustomerItem()
            .also { logger.info("Cliente actualizado exitosamente: ${customer.id}") }
    }

    /**
     * Genera un token JWT para el cliente
     *
     * @param customer Cliente para el cual generar el token
     * @return Token JWT generado
     */
    private fun generarToken(customer: Customer): String {
        val claims = JwtClaimsSet.builder()
            .id(customer.id)
            .subject(customer.username)
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
            .claim("email", customer.email)
            .claim("nombre", "${customer.firstName} ${customer.lastName}")
            .build()

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).tokenValue
    }

    /**
     * Valida los datos de creación de un nuevo cliente
     *
     * @param request Datos del cliente a validar
     * @throws IllegalArgumentException si los datos no son válidos
     */
    private fun validarDatosCliente(request: CustomerCreationRequest) {
        if (customerRepository.findByUsername(request.username) != null) {
            logger.warn("Intento de crear cliente con nombre de usuario existente: ${request.username}")
            throw IllegalArgumentException("El nombre de usuario ya está en uso")
        }

        if (request.password.length < 8) {
            logger.warn("Intento de crear cliente con contraseña débil")
            throw IllegalArgumentException("La contraseña debe tener al menos 8 caracteres")
        }

        if (!request.email.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)$"))) {
            logger.warn("Intento de crear cliente con email inválido: ${request.email}")
            throw IllegalArgumentException("El formato del email no es válido")
        }
    }

    companion object {
        private const val TOKEN_EXPIRATION_HOURS = 1L
    }
}