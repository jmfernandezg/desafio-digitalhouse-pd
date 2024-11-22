package com.jmfg.certs.dh.prodev.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.jmfg.certs.dh.prodev.model.dto.CustomerResponse
import jakarta.persistence.*
import java.time.LocalDate
import java.time.Period
import java.util.*

/**
 * Entidad que representa un cliente en el sistema
 *
 * Esta clase gestiona la información personal y de autenticación de los clientes,
 * así como sus reservaciones asociadas. Hereda funcionalidad de auditoría de BaseEntity.
 */
@Entity
@Table(
    name = "customers",
    uniqueConstraints = [
        UniqueConstraint(name = "uk_customer_username", columnNames = ["username"]),
        UniqueConstraint(name = "uk_customer_email", columnNames = ["email"])
    ],
    indexes = [
        Index(name = "idx_customer_username", columnList = "username"),
        Index(name = "idx_customer_email", columnList = "email")
    ]
)
data class Customer(
    /**
     * Identificador único del cliente
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = UUID.randomUUID().toString(),

    /**
     * Nombre de usuario único para autenticación
     */
    @Column(nullable = false, length = 50)
    val username: String = "",

    /**
     * Contraseña encriptada del cliente
     */
    @JsonIgnore
    @Column(nullable = false)
    val password: String = "",

    /**
     * Nombre(s) del cliente
     */
    @Column(name = "first_name", nullable = false, length = 100)
    val firstName: String = "",

    /**
     * Apellido(s) del cliente
     */
    @Column(name = "last_name", nullable = false, length = 100)
    val lastName: String = "",

    /**
     * Fecha de nacimiento
     */
    @Column(nullable = false)
    val dob: LocalDate = LocalDate.now(),

    /**
     * Correo electrónico único
     */
    @Column(nullable = false)
    val email: String = "",

    /**
     * Reservaciones asociadas al cliente
     */
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    @JsonIgnore
    val reservations: List<Reservation> = mutableListOf()
) : BaseEntity() {

    /**
     * Nombre completo del cliente
     */
    @get:Transient
    val fullName: String
        get() = "$firstName $lastName"

    /**
     * Edad actual del cliente
     */
    @get:Transient
    val age: Int
        get() = Period.between(dob, LocalDate.now()).years

    /**
     * Verifica si el cliente es mayor de edad
     */
    @get:Transient
    val isAdult: Boolean
        get() = age >= 18

    /**
     * Cantidad de reservaciones activas
     */
    @get:Transient
    val activeReservationsCount: Int
        get() = reservations.count { it.isActive() }

    init {
        require(username.isNotBlank()) { "El nombre de usuario no puede estar vacío" }
        require(username.length >= 3) { "El nombre de usuario debe tener al menos 3 caracteres" }
        require(password.isNotBlank()) { "La contraseña no puede estar vacía" }
        require(firstName.isNotBlank()) { "El nombre no puede estar vacío" }
        require(lastName.isNotBlank()) { "El apellido no puede estar vacío" }
        require(email.matches(EMAIL_REGEX)) { "El formato del correo electrónico es inválido" }
        require(!dob.isAfter(LocalDate.now())) { "La fecha de nacimiento no puede ser futura" }
    }

    companion object {
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@(.+)$")

        /**
         * Crea una instancia de prueba para desarrollo
         */
        fun createTestInstance() = Customer(
            username = "testuser",
            password = "hashedPassword",
            firstName = "Test",
            lastName = "User",
            dob = LocalDate.now().minusYears(25),
            email = "test@example.com"
        )
    }
}

/**
 * Extensión para convertir un Customer a CustomerResponse.CustomerItem
 */
fun Customer.toCustomerItem() = CustomerResponse.CustomerItem(
    id = this.id,
    username = this.username,
    email = this.email,
    firstName = this.firstName,
    lastName = this.lastName
)

/**
 * Extensión para convertir un Customer a CustomerResponse.CustomerItem con token
 */
fun Customer.toCustomerItemWithToken(token: String) = toCustomerItem().copy(token = token)