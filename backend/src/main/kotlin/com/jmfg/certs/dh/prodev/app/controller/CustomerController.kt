package com.jmfg.certs.dh.prodev.app.controller

import com.jmfg.certs.dh.prodev.model.dto.*
import com.jmfg.certs.dh.prodev.service.CustomerService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

/**
 * Controlador para operaciones de cliente
 *
 * Maneja las operaciones principales que pueden realizar los clientes:
 * - Registro
 * - Login
 * - Actualización de perfil
 */
@RestController
@RequestMapping("/api/customers")
class CustomerController(private val customerService: CustomerService) {

    private val logger = LoggerFactory.getLogger(CustomerController::class.java)

    /**
     * Endpoint para inicio de sesión
     */
    @PostMapping("/login")
    suspend fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        return try {
            customerService.login(request)?.let { customerItem ->
                ResponseEntity.ok(LoginResponse.success(customerItem.token!!))
            } ?: ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(LoginResponse.error("Credenciales inválidas"))
        } catch (e: IllegalArgumentException) {
            logger.warn("Error en inicio de sesión: ${e.message}")
            ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(LoginResponse.error(e.message ?: "Error de autenticación"))
        }
    }

    /**
     * Endpoint para registro de nuevos clientes
     */
    @PostMapping("/register")
    suspend fun createCustomer(@RequestBody request: CustomerCreationRequest): ResponseEntity<CustomerResponse.CustomerItem> {
        return try {
            ResponseEntity.status(HttpStatus.CREATED)
                .body(customerService.create(request))
        } catch (e: IllegalArgumentException) {
            logger.warn("Error en registro de cliente: ${e.message}")
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    /**
     * Endpoint para actualización de perfil
     */
    @PutMapping("/{id}")
    suspend fun updateCustomer(
        @PathVariable id: Long,
        @RequestBody request: CustomerUpdateRequest
    ): ResponseEntity<CustomerResponse.CustomerItem> {
        return try {
            ResponseEntity.ok(customerService.update(id, request))
        } catch (e: NoSuchElementException) {
            logger.warn("Cliente no encontrado: $id")
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado")
        } catch (e: IllegalArgumentException) {
            logger.warn("Error en actualización de cliente: ${e.message}")
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }
}