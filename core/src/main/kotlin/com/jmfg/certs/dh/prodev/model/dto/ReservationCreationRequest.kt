package com.jmfg.certs.dh.prodev.model.dto

import com.jmfg.certs.dh.prodev.model.Reservation
import java.time.LocalDate
import java.time.LocalDateTime


data class ReservationCreationRequest(
    val customerId: String, val lodgingId: String, val startDate: String, val endDate: String
)


/**
 * Clase que representa la respuesta para operaciones relacionadas con reservaciones.
 * Encapsula tanto la información de las reservaciones como metadatos adicionales.
 */
data class ReservationResponse(
    /**
     * Lista de reservaciones incluidas en la respuesta
     */
    val items: List<ReservationItem>,

    /**
     * Número total de reservaciones en el sistema
     */
    val total: Long,

    /**
     * Timestamp de cuando se generó la respuesta
     */
    val timestamp: LocalDateTime = LocalDateTime.now()
) {
    /**
     * Clase que representa una reservación individual en la respuesta.
     * Contiene toda la información relevante de una reservación.
     */
    data class ReservationItem(
        /**
         * Identificador único de la reservación
         */
        val id: String,

        /**
         * Identificador del cliente que realizó la reservación
         */
        val customerId: String,

        /**
         * Información del cliente
         */
        val customerInfo: CustomerInfo,

        /**
         * Identificador del alojamiento reservado
         */
        val lodgingId: String,

        /**
         * Información del alojamiento
         */
        val lodgingInfo: LodgingInfo,

        /**
         * Fecha de entrada
         */
        val checkIn: LocalDate,

        /**
         * Fecha de salida
         */
        val checkOut: LocalDate,

        /**
         * Número total de huéspedes
         */
        val numberOfGuests: Int,

        /**
         * Estado actual de la reservación
         */
        val status: ReservationStatus,

        /**
         * Precio total de la reservación
         */
        val totalPrice: Double,

        /**
         * Fecha y hora de creación de la reservación
         */
        val createdAt: LocalDateTime,

        /**
         * Última actualización de la reservación
         */
        val updatedAt: LocalDateTime,

        /**
         * Comentarios o notas especiales
         */
        val notes: String? = null,

        /**
         * Motivo de cancelación si aplica
         */
        val cancellationReason: String? = null
    ) {
        /**
         * Información resumida del cliente
         */
        data class CustomerInfo(
            val id: String,
            val name: String,
            val email: String,
            val phone: String
        )

        /**
         * Información resumida del alojamiento
         */
        data class LodgingInfo(
            val id: String,
            val name: String,
            val address: String,
            val category: String,
            val pricePerNight: Double
        )
    }

    /**
     * Estados posibles de una reservación
     */
    enum class ReservationStatus {
        PENDING,      // Reservación creada pero no confirmada
        CONFIRMED,    // Reservación confirmada
        IN_PROGRESS,  // Huéspedes están actualmente en el alojamiento
        COMPLETED,    // Estancia finalizada
        CANCELLED     // Reservación cancelada
    }

    companion object {
        /**
         * Convierte una lista de Reservation a ReservationResponse
         */
        fun fromReservations(reservations: List<Reservation>): ReservationResponse {
            val items = reservations.map { reservation ->
                ReservationItem(
                    id = reservation.id,
                    customerId = reservation.customerId,
                    customerInfo = ReservationItem.CustomerInfo(
                        id = reservation.customer.id,
                        name = "${reservation.customer.firstName} ${reservation.customer.lastName}",
                        email = reservation.customer.email,
                        phone = reservation.customer.phone
                    ),
                    lodgingId = reservation.lodgingId,
                    lodgingInfo = ReservationItem.LodgingInfo(
                        id = reservation.lodging.id,
                        name = reservation.lodging.name,
                        address = reservation.lodging.address,
                        category = reservation.lodging.category.name,
                        pricePerNight = reservation.lodging.pricePerNight
                    ),
                    checkIn = reservation.checkIn,
                    checkOut = reservation.checkOut,
                    numberOfGuests = reservation.numberOfGuests,
                    status = reservation.status,
                    totalPrice = reservation.totalPrice,
                    createdAt = reservation.createdAt,
                    updatedAt = reservation.updatedAt,
                    notes = reservation.notes,
                    cancellationReason = reservation.cancellationReason
                )
            }
            return ReservationResponse(
                items = items,
                total = items.size.toLong()
            )
        }

        /**
         * Crea una respuesta vacía
         */
        fun empty() = ReservationResponse(
            items = emptyList(),
            total = 0
        )
    }
}