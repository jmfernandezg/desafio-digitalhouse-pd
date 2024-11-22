package com.jmfg.certs.dh.prodev.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

/**
 * Entidad que representa una reserva de alojamiento
 *
 * Gestiona la información relacionada con las reservas realizadas por los clientes,
 * incluyendo fechas, precios y relaciones con el cliente y el alojamiento.
 */
@Entity
@Table(
    name = "reservations",
    indexes = [
        Index(name = "idx_reservation_customer", columnList = "customer_id"),
        Index(name = "idx_reservation_lodging", columnList = "lodging_id"),
        Index(name = "idx_reservation_dates", columnList = "start_date, end_date")
    ]
)
data class Reservation(
    /**
     * Identificador único de la reserva
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = UUID.randomUUID().toString(),

    /**
     * Fecha y hora de inicio de la reserva
     */
    @Column(name = "start_date", nullable = false)
    val startDate: LocalDateTime = LocalDateTime.now(),

    /**
     * Fecha y hora de finalización de la reserva
     */
    @Column(name = "end_date", nullable = false)
    val endDate: LocalDateTime = LocalDateTime.now(),

    /**
     * Precio total de la reserva
     */
    @Column(name = "total_price", nullable = false)
    val totalPrice: Double = 0.0,

    /**
     * Cliente que realiza la reserva
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    val customer: Customer = Customer(),

    /**
     * Alojamiento reservado
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lodging_id", nullable = false)
    val lodging: Lodging = Lodging()
) : BaseEntity() {

    init {
        require(startDate.isBefore(endDate)) {
            "La fecha de inicio debe ser anterior a la fecha de fin"
        }
        require(totalPrice >= 0) {
            "El precio total no puede ser negativo"
        }
        require(!startDate.isBefore(LocalDateTime.now())) {
            "No se pueden crear reservas con fecha de inicio en el pasado"
        }
    }

    /**
     * Calcula la duración de la reserva en días
     */
    fun getDurationInDays(): Long =
        ChronoUnit.DAYS.between(startDate, endDate)

    /**
     * Calcula el precio promedio por noche
     */
    fun getAverageNightlyPrice(): Double =
        totalPrice / getDurationInDays().coerceAtLeast(1)

    /**
     * Verifica si la reserva está actualmente activa
     */
    fun isActive(): Boolean {
        val now = LocalDateTime.now()
        return now.isAfter(startDate) && now.isBefore(endDate)
    }

    /**
     * Verifica si la reserva ya finalizó
     */
    fun hasEnded(): Boolean =
        LocalDateTime.now().isAfter(endDate)

    /**
     * Verifica si la reserva aún no ha comenzado
     */
    fun hasPendingStart(): Boolean =
        LocalDateTime.now().isBefore(startDate)

    companion object {
        /**
         * Crea una instancia de prueba
         */
        fun createTestInstance(
            customer: Customer = Customer(),
            lodging: Lodging = Lodging()
        ): Reservation {
            val startDate = LocalDateTime.now().plusDays(1)
            return Reservation(
                startDate = startDate,
                endDate = startDate.plusDays(3),
                totalPrice = 300.0,
                customer = customer,
                lodging = lodging
            )
        }

        /**
         * Calcula el precio total para una reserva
         */
        fun calculateTotalPrice(
            lodging: Lodging,
            startDate: LocalDateTime,
            endDate: LocalDateTime
        ): Double {
            val days = ChronoUnit.DAYS.between(startDate, endDate)
            return lodging.price * days
        }
    }

    /**
     * Genera un resumen de la reserva
     */
    fun getSummary(): Map<String, Any> = mapOf(
        "id" to id,
        "duration" to getDurationInDays(),
        "averagePrice" to getAverageNightlyPrice(),
        "status" to when {
            isActive() -> "ACTIVA"
            hasEnded() -> "FINALIZADA"
            else -> "PENDIENTE"
        },
        "lodgingName" to lodging.name,
        "customerName" to customer.fullName
    )
}