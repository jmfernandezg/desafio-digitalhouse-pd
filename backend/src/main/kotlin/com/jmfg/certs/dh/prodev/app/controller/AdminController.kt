package com.jmfg.certs.dh.prodev.app.controller

import com.jmfg.certs.dh.prodev.model.dto.CustomerResponse
import com.jmfg.certs.dh.prodev.model.dto.CustomerStatistics
import com.jmfg.certs.dh.prodev.service.CustomerService
import org.slf4j.LoggerFactory
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate

/**
 * Controlador para operaciones administrativas
 *
 * Maneja las operaciones que solo pueden realizar los administradores:
 * - Consulta de todos los clientes
 * - Eliminación de clientes
 * - Reportes y consultas especializadas
 */
@RestController
@RequestMapping("/api/admin/customers")
@PreAuthorize("hasRole('ADMIN')")  // Asegura que solo los administradores pueden acceder
class AdminController(private val customerService: CustomerService) {

    private val logger = LoggerFactory.getLogger(AdminController::class.java)

    /**
     * Obtiene todos los clientes registrados
     */
    @GetMapping
    suspend fun getAllCustomers(): ResponseEntity<CustomerResponse> {
        return ResponseEntity.ok(customerService.findAll())
    }

    /**
     * Elimina un cliente por su ID
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun deleteCustomer(@PathVariable id: Long) {
        try {
            customerService.delete(id)
        } catch (e: NoSuchElementException) {
            logger.warn("Intento de eliminar cliente inexistente: $id")
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado")
        }
    }

    /**
     * Busca clientes por país de residencia
     */
    @GetMapping("/by-country/{country}")
    suspend fun getCustomersByCountry(@PathVariable country: String): ResponseEntity<CustomerResponse> {
        val customers = customerService.findByCountry(country)
        return if (customers.hasCustomers()) {
            ResponseEntity.ok(customers)
        } else {
            ResponseEntity.noContent().build()
        }
    }

    /**
     * Obtiene clientes con pasaportes próximos a vencer
     */
    @GetMapping("/expiring-passports")
    suspend fun getCustomersWithExpiringPassports(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) beforeDate: LocalDate
    ): ResponseEntity<CustomerResponse> {
        val customers = customerService.findByPassportExpiryBefore(beforeDate)
        return if (customers.hasCustomers()) {
            ResponseEntity.ok(customers)
        } else {
            ResponseEntity.noContent().build()
        }
    }

    /**
     * Obtiene estadísticas generales de clientes
     */
    @GetMapping("/statistics")
    suspend fun getCustomerStatistics(): ResponseEntity<CustomerStatistics> {
        return ResponseEntity.ok(customerService.getStatistics())
    }
}
