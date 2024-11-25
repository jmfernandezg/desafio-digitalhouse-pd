package com.jmfg.certs.dh.prodev.model.dto

import java.time.LocalDate
import java.time.Period

/**
 * Solicitud de creación de un nuevo cliente
 *
 * Esta clase representa los datos necesarios para registrar un nuevo cliente
 * en el sistema de agencia de viajes.
 *
 * @property email Correo electrónico del cliente (usado como identificador único)
 * @property password Contraseña del cliente
 * @property firstName Nombre(s) del cliente
 * @property lastName Apellido(s) del cliente
 * @property dob Fecha de nacimiento
 * @property passportNumber Número de pasaporte (opcional)
 * @property passportExpiry Fecha de vencimiento del pasaporte (opcional)
 * @property phoneNumber Número de teléfono móvil (opcional)
 * @property countryOfResidence País de residencia (opcional)
 * @property preferredFrequentFlyerProgram Programa de viajero frecuente preferido (opcional)
 */
data class CustomerCreationRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val dob: LocalDate,
    val passportNumber: String? = null,
    val passportExpiry: LocalDate? = null,
    val phoneNumber: String? = null,
    val countryOfResidence: String? = null,
    val preferredFrequentFlyerProgram: String? = null
) {

    init {
        require(email.matches(EMAIL_REGEX)) { "El formato del correo electrónico es inválido" }
        require(password.length >= 8) { "La contraseña debe tener al menos 8 caracteres" }
        require(firstName.isNotBlank()) { "El nombre no puede estar vacío" }
        require(lastName.isNotBlank()) { "El apellido no puede estar vacío" }
        require(!dob.isAfter(LocalDate.now())) { "La fecha de nacimiento no puede ser futura" }
        passportExpiry?.let {
            require(!it.isBefore(LocalDate.now())) { "La fecha de vencimiento del pasaporte no puede ser pasada" }
        }
        phoneNumber?.let {
            require(it.matches(PHONE_REGEX)) { "El formato del número de teléfono es inválido" }
        }
    }

    companion object {
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@(.+)$")
        private val PHONE_REGEX = Regex("^\\+?[1-9]\\d{1,14}$")

        /**
         * Crea una instancia de prueba para desarrollo
         */
        fun createTestInstance() = CustomerCreationRequest(
            email = "test@example.com",
            password = "password123",
            firstName = "Test",
            lastName = "User",
            dob = LocalDate.now().minusYears(25),
            passportNumber = "AB123456",
            passportExpiry = LocalDate.now().plusYears(5),
            phoneNumber = "+1234567890",
            countryOfResidence = "España",
            preferredFrequentFlyerProgram = "Iberia Plus"
        )
    }
}

/**
 * Solicitud de inicio de sesión
 *
 * Contiene las credenciales necesarias para autenticar a un cliente.
 *
 * @property email Correo electrónico del cliente
 * @property password Contraseña del cliente
 */
data class LoginRequest(
    val email: String,
    val password: String
) {
    init {
        require(email.matches(EMAIL_REGEX)) { "El formato del correo electrónico es inválido" }
        require(password.isNotBlank()) { "La contraseña no puede estar vacía" }
    }

    /**
     * Enmascara la contraseña para logs y debugging
     */
    override fun toString(): String = "LoginRequest(email=$email, password=****)"

    companion object {
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@(.+)$")
    }
}

/**
 * Respuesta a la solicitud de inicio de sesión
 */
data class LoginResponse(
    val authToken: String? = null,
    val type: String = "Bearer",
    val expiresIn: Long = 3600,
    val error: String? = null
) {
    val isSuccessful: Boolean
        get() = authToken != null && error == null

    companion object {
        fun error(errorMessage: String) = LoginResponse(error = errorMessage)
        fun success(token: String, expiresIn: Long = 3600) = LoginResponse(
            authToken = token,
            expiresIn = expiresIn
        )
    }
}

/**
 * Respuesta que contiene información de clientes
 *
 * @property customers Lista de clientes con sus detalles
 */
data class CustomerResponse(
    val customers: List<CustomerItem>
) {
    /**
     * Representa los datos de un cliente individual
     *
     * @property id Identificador único del cliente
     * @property email Correo electrónico
     * @property firstName Nombre(s)
     * @property lastName Apellido(s)
     * @property dob Fecha de nacimiento
     * @property passportNumber Número de pasaporte
     * @property passportExpiry Fecha de vencimiento del pasaporte
     * @property phoneNumber Número de teléfono
     * @property countryOfResidence País de residencia
     * @property preferredFrequentFlyerProgram Programa de viajero frecuente preferido
     * @property token Token de autenticación (opcional)
     */
    data class CustomerItem(
        val id: String,
        val email: String,
        val firstName: String,
        val lastName: String,
        val dob: LocalDate? = null,
        val passportNumber: String? = null,
        val passportExpiry: LocalDate? = null,
        val phoneNumber: String? = null,
        val countryOfResidence: String? = null,
        val preferredFrequentFlyerProgram: String? = null,
        val token: String? = null
    ) {
        val fullName: String
            get() = "$firstName $lastName"

        val isAuthenticated: Boolean
            get() = token != null

        val age: Int?
            get() = dob?.let { Period.between(it, LocalDate.now()).years }

        val isPassportValid: Boolean
            get() = passportExpiry?.isAfter(LocalDate.now()) ?: false
    }

    companion object {
        fun empty() = CustomerResponse(emptyList())
        fun single(customer: CustomerItem) = CustomerResponse(listOf(customer))
    }

    fun hasCustomers(): Boolean = customers.isNotEmpty()

    fun findByEmail(email: String): CustomerItem? =
        customers.find { it.email.equals(email, ignoreCase = true) }

    fun getAuthenticatedCustomers(): List<CustomerItem> =
        customers.filter { it.isAuthenticated }
}

data class CustomerStatistics(
    val totalCustomers: Int,
    val customersByCountry: Map<String, Int>,
    val averageAge: Double,
    val expiringPassportsCount: Int,
    val customersWithoutPassport: Int
)

/**
 * Solicitud de actualización de cliente
 *
 * Permite actualizar los datos modificables del cliente
 */
data class CustomerUpdateRequest(
    val id: Long? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val passportNumber: String? = null,
    val passportExpiry: LocalDate? = null,
    val phoneNumber: String? = null,
    val countryOfResidence: String? = null,
    val preferredFrequentFlyerProgram: String? = null
) {
    init {
        firstName?.let { require(it.isNotBlank()) { "El nombre no puede estar vacío" } }
        lastName?.let { require(it.isNotBlank()) { "El apellido no puede estar vacío" } }
        passportExpiry?.let {
            require(!it.isBefore(LocalDate.now())) { "La fecha de vencimiento del pasaporte no puede ser pasada" }
        }
        phoneNumber?.let {
            require(it.matches(PHONE_REGEX)) { "El formato del número de teléfono es inválido" }
        }
    }

    companion object {
        private val PHONE_REGEX = Regex("^\\+?[1-9]\\d{1,14}$")
    }
}