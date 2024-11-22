package com.jmfg.certs.dh.prodev.app.controller

import com.jmfg.certs.dh.prodev.model.Customer
import com.jmfg.certs.dh.prodev.model.dto.CustomerCreationRequest
import com.jmfg.certs.dh.prodev.model.dto.CustomerResponse
import com.jmfg.certs.dh.prodev.model.dto.LoginRequest
import com.jmfg.certs.dh.prodev.model.dto.LoginResponse
import com.jmfg.certs.dh.prodev.service.CustomerService
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
 * Controlador para la gestión de clientes
 *
 * Este controlador maneja todas las operaciones relacionadas con los clientes:
 * - Autenticación y login
 * - Creación de nuevos clientes
 * - Actualización de información
 * - Eliminación de clientes
 * - Consulta de clientes
 */
@RestController
@RequestMapping("/v1/customer")
@CrossOrigin(origins = ["http://localhost:3000"])
@Tag(name = "Clientes", description = "APIs para la gestión de clientes del sistema")
class CustomerController(
    private val customerService: CustomerService
) {
    /**
     * Auténtica un cliente en el sistema
     *
     * @param request Datos de login del cliente
     * @return Token JWT para autenticación
     * @throws ResponseStatusException si las credenciales son inválidas o el cliente no existe
     */
    @PostMapping("/login")
    @Operation(
        summary = "Iniciar sesión de cliente",
        description = "Autentica un cliente y devuelve un token JWT para acceso al sistema"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Login exitoso"),
            ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
            ApiResponse(responseCode = "404", description = "Cliente no encontrado")
        ]
    )
    suspend fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<LoginResponse> =
        customerService.login(request)?.let { customer ->
            customer.token?.let { token ->
                ResponseEntity.ok(LoginResponse(authToken = token))
            } ?: ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(LoginResponse(error = "Credenciales inválidas"))
        } ?: ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(LoginResponse(error = "Cliente no encontrado"))

    /**
     * Crea un nuevo cliente en el sistema
     *
     * @param request Datos del nuevo cliente
     * @return Cliente creado
     * @throws ResponseStatusException si el cliente ya existe
     */
    @PostMapping
    @Operation(
        summary = "Crear nuevo cliente",
        description = "Registra un nuevo cliente con los datos proporcionados"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Cliente creado exitosamente",
                content = [Content(schema = Schema(implementation = Customer::class))]
            ),
            ApiResponse(responseCode = "400", description = "Cliente ya existe o datos inválidos")
        ]
    )
    suspend fun create(@Valid @RequestBody request: CustomerCreationRequest): ResponseEntity<CustomerResponse.CustomerItem> =
        customerService.create(request)?.let {
            ResponseEntity.status(HttpStatus.CREATED).body(it)
        } ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "El cliente ya existe en el sistema"
        )

    /**
     * Elimina un cliente del sistema
     *
     * @param id Identificador del cliente
     * @return Respuesta sin contenido si la eliminación fue exitosa
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar cliente",
        description = "Elimina un cliente del sistema usando su identificador"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Cliente eliminado exitosamente"),
            ApiResponse(responseCode = "404", description = "Cliente no encontrado")
        ]
    )
    suspend fun delete(@PathVariable id: String): ResponseEntity<Void> =
        customerService.delete(id).let {
            ResponseEntity.noContent().build()
        }

    /**
     * Actualiza los datos de un cliente existente
     *
     * @param id ID del cliente a actualizar
     * @param customer Nuevos datos del cliente
     * @return Cliente actualizado
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar cliente",
        description = "Actualiza los datos de un cliente existente"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente"),
            ApiResponse(responseCode = "404", description = "Cliente no encontrado")
        ]
    )
    suspend fun update(
        @PathVariable id: String,
        @Valid @RequestBody customer: Customer
    ): ResponseEntity<CustomerResponse.CustomerItem> =
        if (id == customer.id) {
            customerService.update(customer).let {
                ResponseEntity.ok(it)
            } ?: ResponseEntity.notFound().build()
        } else {
            ResponseEntity.badRequest().build()
        }

    /**
     * Obtiene todos los clientes del sistema
     *
     * @return Lista de clientes
     */
    @GetMapping
    @Operation(
        summary = "Obtener todos los clientes",
        description = "Recupera la lista completa de clientes registrados en el sistema"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Lista de clientes recuperada exitosamente",
                content = [Content(schema = Schema(implementation = Customer::class))]
            )
        ]
    )
    suspend fun findAll(): ResponseEntity<CustomerResponse> =
        ResponseEntity.ok(customerService.findAll())
}