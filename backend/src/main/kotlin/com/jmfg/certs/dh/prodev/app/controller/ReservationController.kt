package com.jmfg.certs.dh.prodev.app.controller

import com.jmfg.certs.dh.prodev.model.Reservation
import com.jmfg.certs.dh.prodev.model.dto.ReservationCreationRequest
import com.jmfg.certs.dh.prodev.model.dto.ReservationResponse
import com.jmfg.certs.dh.prodev.service.ReservationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

/**
 * Controlador para la gestión de reservas
 *
 * Este controlador maneja todas las operaciones relacionadas con reservas:
 * - Creación de nuevas reservas
 * - Consulta de reservas existentes
 * - Actualización de reservas
 * - Cancelación de reservas
 *
 * Proporciona una API REST completa para el ciclo de vida de las reservas
 * de alojamientos turísticos.
 */
@RestController
@RequestMapping("/v1/reservation")
@CrossOrigin(origins = ["http://localhost:3000"])
@Tag(name = "Reservas", description = "APIs para la gestión de reservas de alojamientos")
class ReservationController(
    private val reservationService: ReservationService
) {
    /**
     * Crea una nueva reserva en el sistema
     *
     * @param createRequest Datos de la reserva a crear
     * @return Reserva creada
     * @throws ResponseStatusException si hay conflictos con la disponibilidad
     */
    @PostMapping
    @Operation(
        summary = "Crear reserva",
        description = "Registra una nueva reserva de alojamiento con los datos proporcionados"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Reserva creada exitosamente",
                content = [Content(schema = Schema(implementation = Reservation::class))]
            ),
            ApiResponse(responseCode = "400", description = "Datos de reserva inválidos"),
            ApiResponse(responseCode = "409", description = "Conflicto de disponibilidad")
        ]
    )
    suspend fun create(
        @Valid @RequestBody createRequest: ReservationCreationRequest
    ): ResponseEntity<Reservation> =
        try {
            reservationService.create(createRequest).let {
                ResponseEntity.status(HttpStatus.CREATED).body(it)
            } ?: throw ResponseStatusException(
                HttpStatus.CONFLICT,
                "No hay disponibilidad para las fechas seleccionadas"
            )
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Datos de reserva inválidos: ${e.message}"
            )
        }

    /**
     * Obtiene todas las reservas del sistema
     *
     * @return Lista de todas las reservas existentes
     */
    @GetMapping
    @Operation(
        summary = "Listar reservas",
        description = "Recupera la lista completa de reservas registradas en el sistema"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Lista de reservas recuperada exitosamente",
                content = [Content(schema = Schema(implementation = Reservation::class))]
            )
        ]
    )
    suspend fun findAll(): ResponseEntity<ReservationResponse> =
        ResponseEntity.ok(reservationService.findAll())

    /**
     * Busca una reserva específica por su identificador
     *
     * @param id Identificador único de la reserva
     * @return Detalles de la reserva encontrada
     * @throws ResponseStatusException si la reserva no existe
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar reserva",
        description = "Obtiene los detalles de una reserva específica usando su identificador"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Reserva encontrada exitosamente"
            ),
            ApiResponse(
                responseCode = "404",
                description = "Reserva no encontrada"
            )
        ]
    )
    suspend fun findById(@PathVariable id: String): ResponseEntity<Reservation> =
        reservationService.findById(id)?.let {
            ResponseEntity.ok(it)
        } ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Reserva no encontrada"
        )

    /**
     * Actualiza una reserva existente
     *
     * @param id Identificador de la reserva a actualizar
     * @param reservation Nuevos datos de la reserva
     * @return Reserva actualizada
     * @throws ResponseStatusException si la reserva no existe o hay conflictos
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar reserva",
        description = "Modifica los datos de una reserva existente"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Reserva actualizada exitosamente"),
            ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
            ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
            ApiResponse(responseCode = "409", description = "Conflicto en la actualización")
        ]
    )
    suspend fun update(
        @PathVariable id: String,
        @Valid @RequestBody reservation: Reservation
    ): ResponseEntity<Reservation> =
        if (id == reservation.id) {
            reservationService.update(reservation).let {
                ResponseEntity.ok(it)
            } ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Reserva no encontrada"
            )
        } else {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "El ID de la reserva no coincide con el ID en la URL"
            )
        }

    /**
     * Cancela y elimina una reserva existente
     *
     * @param id Identificador de la reserva a cancelar
     * @throws ResponseStatusException si la reserva no existe
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Cancelar reserva",
        description = "Cancela y elimina una reserva existente del sistema"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Reserva cancelada exitosamente"),
            ApiResponse(responseCode = "404", description = "Reserva no encontrada")
        ]
    )
    suspend fun delete(@PathVariable id: String): ResponseEntity<Void> =
        try {
            reservationService.delete(id)
            ResponseEntity.noContent().build()
        } catch (e: NoSuchElementException) {
            throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Reserva no encontrada"
            )
        }
}