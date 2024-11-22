package com.jmfg.certs.dh.prodev.app.service

import com.jmfg.certs.dh.prodev.app.repository.CustomerRepository
import com.jmfg.certs.dh.prodev.app.repository.LodgingRepository
import com.jmfg.certs.dh.prodev.app.repository.ReservationRepository
import com.jmfg.certs.dh.prodev.model.Reservation
import com.jmfg.certs.dh.prodev.model.dto.ReservationCreationRequest
import com.jmfg.certs.dh.prodev.service.ReservationService
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration.between
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * Implementación del servicio de gestión de reservas
 *
 * Esta clase implementa la lógica de negocio para todas las operaciones
 * relacionadas con reservas de alojamientos, incluyendo:
 * - Creación y gestión de reservas
 * - Validación de disponibilidad
 * - Confirmación y cancelación de reservas
 * - Consulta de reservas existentes
 */
@Service
class ReservationServiceImpl(
    private val reservationRepository: ReservationRepository,
    private val customerRepository: CustomerRepository,
    private val lodgingRepository: LodgingRepository
) : ReservationService {

    private val logger = LoggerFactory.getLogger(ReservationServiceImpl::class.java)

    /**
     * Obtiene todas las reservas existentes
     *
     * @return Lista de todas las reservas en el sistema
     */
    @Transactional(readOnly = true)
    override fun findAll(): List<Reservation> {
        logger.debug("Consultando todas las reservas")
        return reservationRepository.findAll()
    }

    /**
     * Elimina una reserva existente
     *
     * @param id Identificador de la reserva a eliminar
     * @throws NoSuchElementException si la reserva no existe
     */
    @Transactional
    override fun delete(id: String) {
        logger.debug("Eliminando reserva con ID: $id")

        if (!reservationRepository.existsById(id)) {
            logger.error("Intento de eliminar reserva inexistente: $id")
            throw NoSuchElementException("Reserva no encontrada con ID: $id")
        }

        reservationRepository.deleteById(id)
        logger.info("Reserva eliminada exitosamente: $id")
    }

    /**
     * Actualiza una reserva existente
     *
     * @param reserva Nuevos datos de la reserva
     * @return Reserva actualizada
     * @throws NoSuchElementException si la reserva no existe
     * @throws IllegalArgumentException si los datos son inválidos
     */
    @Transactional
    override fun update(reserva: Reservation): Reservation {
        logger.debug("Actualizando reserva con ID: ${reserva.id}")

        val reservaExistente = reservationRepository.findByIdOrNull(reserva.id)
            ?: throw NoSuchElementException("Reserva no encontrada con ID: ${reserva.id}")

        validarFechasReserva(reserva.startDate, reserva.endDate)
        validarDisponibilidad(reserva.lodging.id, reserva.startDate, reserva.endDate, reserva.id)

        return reservationRepository.save(reserva).also {
            logger.info("Reserva actualizada exitosamente: ${reserva.id}")
        }
    }

    /**
     * Busca una reserva por su identificador
     *
     * @param id Identificador de la reserva
     * @return Reserva encontrada o null si no existe
     */
    @Transactional(readOnly = true)
    override fun findById(id: String): Reservation? {
        logger.debug("Buscando reserva con ID: $id")
        return reservationRepository.findByIdOrNull(id)
    }

    /**
     * Crea una nueva reserva
     *
     * @param request Datos de la nueva reserva
     * @return Reserva creada
     * @throws IllegalArgumentException si los datos son inválidos o hay conflictos
     */
    @Transactional
    override fun create(request: ReservationCreationRequest): Reservation {
        logger.debug("Creando nueva reserva para cliente: ${request.customerId}")

        try {
            val cliente = customerRepository.findByIdOrNull(request.customerId)
                ?: throw IllegalArgumentException("Cliente no encontrado con ID: ${request.customerId}")

            val alojamiento = lodgingRepository.findByIdOrNull(request.lodgingId)
                ?: throw IllegalArgumentException("Alojamiento no encontrado con ID: ${request.lodgingId}")

            val fechaInicio = parsearFecha(request.startDate)
            val fechaFin = parsearFecha(request.endDate)

            validarFechasReserva(fechaInicio, fechaFin)
            validarDisponibilidad(request.lodgingId, fechaInicio, fechaFin)

            return Reservation(
                customer = cliente,
                lodging = alojamiento,
                startDate = fechaInicio,
                endDate = fechaFin,
                totalPrice = calcularPrecioTotal(alojamiento.price, fechaInicio, fechaFin)
            ).let {
                reservationRepository.save(it)
            }.also {
                logger.info("Reserva creada exitosamente para cliente: ${request.customerId}")
            }
        } catch (e: Exception) {
            logger.error("Error al crear reserva: ${e.message}")
            throw e
        }
    }

    /**
     * Valida las fechas de una reserva
     *
     * @param fechaInicio Fecha de inicio de la reserva
     * @param fechaFin Fecha de fin de la reserva
     * @throws IllegalArgumentException si las fechas son inválidas
     */
    private fun validarFechasReserva(fechaInicio: LocalDateTime, fechaFin: LocalDateTime) {
        if (fechaInicio.isBefore(LocalDateTime.now())) {
            throw IllegalArgumentException("La fecha de inicio no puede ser anterior a hoy")
        }

        if (fechaFin.isBefore(fechaInicio)) {
            throw IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio")
        }
    }

    /**
     * Verifica la disponibilidad del alojamiento para las fechas solicitadas
     *
     * @param idAlojamiento ID del alojamiento
     * @param fechaInicio Fecha de inicio de la reserva
     * @param fechaFin Fecha de fin de la reserva
     * @param idReservaExcluir ID de reserva a excluir de la verificación (para actualizaciones)
     * @throws IllegalArgumentException si hay conflicto de fechas
     */
    private fun validarDisponibilidad(
        idAlojamiento: String,
        fechaInicio: LocalDateTime,
        fechaFin: LocalDateTime,
        idReservaExcluir: String? = null
    ) {
        val reservasExistentes = reservationRepository
            .findByLodgingIdAndDateRange(idAlojamiento, fechaInicio, fechaFin)
            .filter { it.id != idReservaExcluir }

        if (reservasExistentes.isNotEmpty()) {
            throw IllegalArgumentException("El alojamiento no está disponible para las fechas seleccionadas")
        }
    }

    /**
     * Parsea una fecha en formato ISO
     *
     * @param fecha Fecha en formato string
     * @return LocalDateTime parseado
     * @throws IllegalArgumentException si el formato es inválido
     */
    private fun parsearFecha(fecha: String): LocalDateTime {
        return try {
            LocalDateTime.parse(fecha, DateTimeFormatter.ISO_DATE_TIME)
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException("Formato de fecha inválido: $fecha")
        }
    }

    /**
     * Calcula el precio total de la reserva
     *
     * @param precioPorNoche Precio por noche del alojamiento
     * @param fechaInicio Fecha de inicio de la reserva
     * @param fechaFin Fecha de fin de la reserva
     * @return Precio total calculado
     */
    private fun calcularPrecioTotal(
        precioPorNoche: Double,
        fechaInicio: LocalDateTime,
        fechaFin: LocalDateTime
    ) = precioPorNoche * between(fechaInicio, fechaFin).toDays()
}