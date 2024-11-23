package com.jmfg.certs.dh.prodev.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.jmfg.certs.dh.prodev.model.dto.CustomerResponse
import jakarta.persistence.*
import java.time.LocalDate
import java.time.Period
import java.util.*

/**
 * Entidad que representa un cliente en el sistema de agencia de viajes
 *
 * Esta clase gestiona la información personal del cliente y sus preferencias de viaje,
 * así como sus reservaciones asociadas. Hereda funcionalidad de auditoría de BaseEntity.
 */
@Entity
@Table(
    name = "customers",
    uniqueConstraints = [
        UniqueConstraint(name = "uk_customer_email", columnNames = ["email"])
    ],
    indexes = [
        Index(name = "idx_customer_email", columnList = "email"),
        Index(name = "idx_customer_passport", columnList = "passport_number")
    ]
)
data class Customer(
    /**
     * Identificador único del cliente
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    /**
     * Correo electrónico único (usado para identificación y comunicación)
     */
    @Column(nullable = false)
    val email: String = "",

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
     * Número de pasaporte
     */
    @Column(name = "passport_number", length = 50)
    val passportNumber: String? = null,

    /**
     * Fecha de vencimiento del pasaporte
     */
    @Column(name = "passport_expiry")
    val passportExpiry: LocalDate? = null,

    /**
     * Número de teléfono móvil
     */
    @Column(name = "phone_number", length = 20)
    val phoneNumber: String? = null,

    /**
     * País de residencia
     */
    @Column(name = "country_of_residence", length = 100)
    val countryOfResidence: String? = null,

    /**
     * Programa de viajero frecuente preferido
     */
    @Column(name = "preferred_frequent_flyer_program", length = 50)
    val preferredFrequentFlyerProgram: String? = null,

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

    /**
     * Verifica si el pasaporte está vigente
     */
    @get:Transient
    val isPassportValid: Boolean
        get() = passportExpiry?.isAfter(LocalDate.now()) ?: false

    init {
        require(email.matches(EMAIL_REGEX)) { "El formato del correo electrónico es inválido" }
        require(password.isNotBlank()) { "La contraseña no puede estar vacía" }
        require(firstName.isNotBlank()) { "El nombre no puede estar vacío" }
        require(lastName.isNotBlank()) { "El apellido no puede estar vacío" }
        require(!dob.isAfter(LocalDate.now())) { "La fecha de nacimiento no puede ser futura" }
        passportExpiry?.let {
            require(!it.isBefore(LocalDate.now())) { "La fecha de vencimiento del pasaporte no puede ser pasada" }
        }
    }

    companion object {
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@(.+)$")

        /**
         * Crea una instancia de prueba para desarrollo
         */
        fun createTestInstance() = Customer(
            email = "test@example.com",
            password = "hashedPassword",
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
 * Extensión para convertir un Customer a CustomerResponse.CustomerItem
 */
fun Customer.toCustomerItem() = CustomerResponse.CustomerItem(
    id = this.id.toString(),
    email = this.email,
    firstName = this.firstName,
    lastName = this.lastName
)

/**
 * Extensión para convertir un Customer a CustomerResponse.CustomerItem con token
 */
fun Customer.toCustomerItemWithToken(token: String) = toCustomerItem().copy(token = token)