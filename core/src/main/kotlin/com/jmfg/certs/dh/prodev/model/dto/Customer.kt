package com.jmfg.certs.dh.prodev.model.dto

/**
 * Solicitud de creación de un nuevo cliente
 *
 * Esta clase representa los datos necesarios para registrar un nuevo cliente
 * en el sistema.
 *
 * @property username Nombre de usuario único para identificación
 * @property password Contraseña del cliente
 * @property email Correo electrónico del cliente
 * @property firstName Nombre(s) del cliente
 * @property lastName Apellido(s) del cliente
 */
data class CustomerCreationRequest(
    val username: String,
    val password: String,
    val email: String,
    val firstName: String,
    val lastName: String
) {
    /**
     * Valida que los datos de creación sean correctos
     *
     * @throws IllegalArgumentException si algún campo no cumple con las validaciones
     */
    init {
        require(username.isNotBlank()) { "El nombre de usuario no puede estar vacío" }
        require(username.length >= 3) { "El nombre de usuario debe tener al menos 3 caracteres" }
        require(password.length >= 8) { "La contraseña debe tener al menos 8 caracteres" }
        require(email.matches(EMAIL_REGEX)) { "El formato del correo electrónico es inválido" }
        require(firstName.isNotBlank()) { "El nombre no puede estar vacío" }
        require(lastName.isNotBlank()) { "El apellido no puede estar vacío" }
    }

    companion object {
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@(.+)$")

        /**
         * Crea una instancia de prueba para desarrollo
         *
         * @return CustomerCreationRequest con datos de prueba
         */
        fun createTestInstance() = CustomerCreationRequest(
            username = "testuser",
            password = "password123",
            email = "test@example.com",
            firstName = "Test",
            lastName = "User"
        )
    }
}

/**
 * Solicitud de inicio de sesión
 *
 * Contiene las credenciales necesarias para autenticar a un cliente.
 *
 * @property username Nombre de usuario
 * @property password Contraseña del usuario
 */
data class LoginRequest(
    val username: String,
    val password: String
) {
    init {
        require(username.isNotBlank()) { "El nombre de usuario no puede estar vacío" }
        require(password.isNotBlank()) { "La contraseña no puede estar vacía" }
    }

    /**
     * Enmascara la contraseña para logs y debugging
     *
     * @return String con la contraseña oculta
     */
    override fun toString(): String = "LoginRequest(username=$username, password=****)"
}

/**
 * Respuesta a la solicitud de inicio de sesión
 *
 * Contiene el token de autenticación o mensaje de error si el login falló.
 *
 * @property authToken Token JWT de autenticación
 * @property type Tipo de token (por defecto "Bearer")
 * @property expiresIn Tiempo de expiración en segundos
 * @property error Mensaje de error si el login falló
 */
data class LoginResponse(
    val authToken: String? = null,
    val type: String = "Bearer",
    val expiresIn: Long = 3600,
    val error: String? = null
) {
    /**
     * Indica si la respuesta representa un login exitoso
     */
    val isSuccessful: Boolean
        get() = authToken != null && error == null

    companion object {
        /**
         * Crea una respuesta de error
         *
         * @param errorMessage Mensaje de error
         * @return LoginResponse con el error especificado
         */
        fun error(errorMessage: String) = LoginResponse(error = errorMessage)

        /**
         * Crea una respuesta exitosa
         *
         * @param token Token de autenticación
         * @param expiresIn Tiempo de expiración opcional
         * @return LoginResponse con el token
         */
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
     * @property username Nombre de usuario
     * @property email Correo electrónico
     * @property firstName Nombre(s)
     * @property lastName Apellido(s)
     * @property token Token de autenticación (opcional)
     */
    data class CustomerItem(
        val id: String,
        val username: String,
        val email: String,
        val firstName: String,
        val lastName: String,
        val token: String? = null
    ) {
        /**
         * Nombre completo del cliente
         */
        val fullName: String
            get() = "$firstName $lastName"

        /**
         * Indica si el cliente está autenticado
         */
        val isAuthenticated: Boolean
            get() = token != null
    }

    companion object {
        /**
         * Crea una respuesta vacía sin clientes
         */
        fun empty() = CustomerResponse(emptyList())

        /**
         * Crea una respuesta con un solo cliente
         *
         * @param customer Cliente a incluir
         */
        fun single(customer: CustomerItem) = CustomerResponse(listOf(customer))
    }

    /**
     * Verifica si la respuesta contiene clientes
     */
    fun hasCustomers(): Boolean = customers.isNotEmpty()

    /**
     * Encuentra un cliente por su nombre de usuario
     *
     * @param username Nombre de usuario a buscar
     * @return CustomerItem si se encuentra, null si no existe
     */
    fun findByUsername(username: String): CustomerItem? =
        customers.find { it.username.equals(username, ignoreCase = true) }

    /**
     * Obtiene solo los clientes autenticados
     *
     * @return Lista de clientes con token válido
     */
    fun getAuthenticatedCustomers(): List<CustomerItem> =
        customers.filter { it.isAuthenticated }
}