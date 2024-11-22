package com.jmfg.certs.dh.prodev.service

import com.jmfg.certs.dh.prodev.model.Reservation
import com.jmfg.certs.dh.prodev.model.dto.ReservationCreationRequest
import com.jmfg.certs.dh.prodev.model.dto.ReservationResponse

/**
 * Servicio que gestiona las operaciones relacionadas con reservaciones.
 * Proporciona funcionalidades para la creación, modificación, eliminación
 * y consulta de reservaciones en el sistema.
 */
interface ReservationService {
    /**
     * Obtiene todas las reservaciones registradas en el sistema.
     *
     * @return Lista de reservaciones encapsulada en ReservationResponse
     * @throws ServiceException si hay un error al recuperar los datos
     */
    suspend fun findAll(): ReservationResponse

    /**
     * Elimina una reservación del sistema por su identificador.
     *
     * @param id Identificador único de la reservación
     * @throws ResourceNotFoundException si la reservación no existe
     * @throws IllegalStateException si la reservación no puede ser eliminada
     *         (por ejemplo, si ya está en progreso)
     */
    suspend fun delete(id: String)

    /**
     * Actualiza los datos de una reservación existente.
     *
     * @param reservation Datos actualizados de la reservación
     * @return Reservación actualizada
     * @throws ResourceNotFoundException si la reservación no existe
     * @throws IllegalArgumentException si los datos de actualización son inválidos
     * @throws IllegalStateException si la reservación no puede ser modificada
     */
    suspend fun update(reservation: Reservation): Reservation

    /**
     * Busca una reservación específica por su identificador.
     *
     * @param id Identificador único de la reservación
     * @return Reservación si se encuentra, null si no existe
     * @throws IllegalArgumentException si el ID proporcionado no es válido
     */
    suspend fun findById(id: String): Reservation?

    /**
     * Crea una nueva reservación en el sistema.
     *
     * @param request Datos necesarios para la creación de la reservación
     * @return Reservación creada
     * @throws IllegalArgumentException si los datos de creación son inválidos
     * @throws ConflictException si existe un conflicto con otras reservaciones
     * @throws ResourceNotFoundException si el alojamiento o cliente no existen
     */
    suspend fun create(request: ReservationCreationRequest): Reservation

    /**
     * Busca reservaciones por cliente.
     *
     * @param customerId Identificador único del cliente
     * @return Lista de reservaciones del cliente
     * @throws IllegalArgumentException si el ID del cliente no es válido
     */
    suspend fun findByCustomerId(customerId: String): ReservationResponse

    /**
     * Verifica la disponibilidad de un alojamiento para un período específico.
     *
     * @param lodgingId Identificador del alojamiento
     * @param checkIn Fecha de entrada
     * @param checkOut Fecha de salida
     * @return true si está disponible, false si no
     * @throws IllegalArgumentException si las fechas o el ID del alojamiento son inválidos
     */
    suspend fun checkAvailability(
        lodgingId: String,
        checkIn: java.time.LocalDate,
        checkOut: java.time.LocalDate
    ): Boolean

    /**
     * Confirma una reservación previamente creada.
     *
     * @param reservationId Identificador de la reservación
     * @throws ResourceNotFoundException si la reservación no existe
     * @throws IllegalStateException si la reservación no puede ser confirmada
     */
    suspend fun confirm(reservationId: String)

    /**
     * Cancela una reservación existente.
     *
     * @param reservationId Identificador de la reservación
     * @param reason Motivo de la cancelación
     * @throws ResourceNotFoundException si la reservación no existe
     * @throws IllegalStateException si la reservación no puede ser cancelada
     */
    suspend fun cancel(reservationId: String, reason: String)
}