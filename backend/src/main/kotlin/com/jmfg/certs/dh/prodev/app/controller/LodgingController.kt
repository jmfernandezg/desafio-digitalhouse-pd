package com.jmfg.certs.dh.prodev.app.controller

import com.jmfg.certs.dh.prodev.Util
import com.jmfg.certs.dh.prodev.model.dto.CategoryResponse
import com.jmfg.certs.dh.prodev.model.dto.LodgingCreationRequest
import com.jmfg.certs.dh.prodev.model.dto.LodgingResponse
import com.jmfg.certs.dh.prodev.model.dto.LodgingResponse.LodgingItem
import com.jmfg.certs.dh.prodev.model.dto.LodgingSearchRequest
import com.jmfg.certs.dh.prodev.service.LodgingService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import jakarta.validation.Valid

/**
 * Controlador para la gestión de alojamientos
 *
 * Este controlador maneja todas las operaciones relacionadas con alojamientos:
 * - Consulta de categorías
 * - Búsqueda y filtrado de alojamientos
 * - Gestión de alojamientos (crear, eliminar, consultar)
 * - Consulta de ciudades disponibles
 */
@RestController
@RequestMapping("/v1/lodging")
@CrossOrigin(origins = ["http://localhost:3000"])
@Tag(name = "Alojamientos", description = "APIs para la gestión de alojamientos turísticos")
class LodgingController(private val lodgingService: LodgingService) {

    /**
     * Obtiene todas las categorías de alojamientos disponibles
     *
     * @return Lista de categorías de alojamientos
     */
    @GetMapping("/categories")
    @Operation(
        summary = "Obtener categorías",
        description = "Recupera la lista de todas las categorías de alojamientos disponibles"
    )
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "Categorías recuperadas exitosamente",
            content = [Content(schema = Schema(implementation = CategoryResponse::class))]
        )
    ])
    fun getAllCategories(): ResponseEntity<CategoryResponse> =
        ResponseEntity.ok(lodgingService.findAllCategories())

    /**
     * Obtiene alojamientos por categoría
     *
     * @param category Nombre de la categoría a buscar
     * @return Lista de alojamientos de la categoría especificada
     */
    @GetMapping("/categories/{category}")
    @Operation(
        summary = "Buscar por categoría",
        description = "Recupera todos los alojamientos de una categoría específica"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Alojamientos encontrados"),
        ApiResponse(responseCode = "400", description = "Categoría inválida")
    ])
    fun getLodgingsByCategory(@PathVariable category: String): ResponseEntity<LodgingResponse> =
        try {
            ResponseEntity.ok(lodgingService.findByCategory(Util.toCategory(category)))
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoría inválida: $category")
        }

    /**
     * Obtiene un alojamiento por su identificador
     *
     * @param id Identificador único del alojamiento
     * @return Detalles del alojamiento
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar por ID",
        description = "Recupera un alojamiento específico por su identificador único"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Alojamiento encontrado"),
        ApiResponse(responseCode = "404", description = "Alojamiento no encontrado")
    ])
    fun getLodgingById(@PathVariable id: String): ResponseEntity<LodgingItem> =
        lodgingService.findById(id)?.let {
            ResponseEntity.ok(it)
        } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Alojamiento no encontrado")

    /**
     * Obtiene todos los alojamientos disponibles
     *
     * @return Lista completa de alojamientos
     */
    @GetMapping
    @Operation(
        summary = "Listar alojamientos",
        description = "Recupera la lista completa de alojamientos disponibles"
    )
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "Lista de alojamientos recuperada exitosamente"
        )
    ])
    fun getAllLodgings(): ResponseEntity<LodgingResponse> =
        ResponseEntity.ok(lodgingService.findAll())

    /**
     * Crea un nuevo alojamiento
     *
     * @param request Datos del nuevo alojamiento
     * @return Alojamiento creado
     */
    @PostMapping
    @Operation(
        summary = "Crear alojamiento",
        description = "Registra un nuevo alojamiento con los datos proporcionados"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Alojamiento creado exitosamente"),
        ApiResponse(responseCode = "400", description = "Datos inválidos")
    ])
    fun createLodging(@Valid @RequestBody request: LodgingCreationRequest): ResponseEntity<LodgingItem> =
        ResponseEntity.status(HttpStatus.CREATED)
            .body(lodgingService.create(request))

    /**
     * Elimina un alojamiento existente
     *
     * @param id Identificador del alojamiento a eliminar
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar alojamiento",
        description = "Elimina un alojamiento existente usando su identificador"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Alojamiento eliminado exitosamente"),
        ApiResponse(responseCode = "404", description = "Alojamiento no encontrado")
    ])
    fun deleteLodging(@PathVariable id: String): ResponseEntity<Void> =
        lodgingService.delete(id).let {
            ResponseEntity.noContent().build()
        }

    /**
     * Obtiene todas las ciudades con alojamientos disponibles
     *
     * @return Conjunto de nombres de ciudades
     */
    @GetMapping("/cities")
    @Operation(
        summary = "Listar ciudades",
        description = "Recupera la lista de todas las ciudades que tienen alojamientos disponibles"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Lista de ciudades recuperada exitosamente")
    ])
    fun getAllCities(): ResponseEntity<Set<String>> =
        ResponseEntity.ok(lodgingService.findAllCities())

    /**
     * Busca alojamientos según criterios específicos
     *
     * @param request Criterios de búsqueda
     * @return Lista de alojamientos que cumplen los criterios
     */
    @PostMapping("/buscar")
    @Operation(
        summary = "Buscar alojamientos",
        description = "Busca alojamientos que cumplan con los criterios especificados"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente"),
        ApiResponse(responseCode = "400", description = "Criterios de búsqueda inválidos")
    ])
    fun searchLodgings(@Valid @RequestBody request: LodgingSearchRequest): ResponseEntity<LodgingResponse> =
        ResponseEntity.ok(lodgingService.search(request))
}