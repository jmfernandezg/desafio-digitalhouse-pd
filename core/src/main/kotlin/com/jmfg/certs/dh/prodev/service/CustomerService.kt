package com.jmfg.certs.dh.prodev.service

import com.jmfg.certs.dh.prodev.model.dto.*
import java.time.LocalDate

/**
 * Interfaz que define los servicios disponibles para la gestión de clientes
 *
 * Define las operaciones principales para la gestión de clientes, incluyendo:
 * - Autenticación y login
 * - Operaciones CRUD
 * - Consultas personalizadas
 */
interface CustomerService {
    /**
     * Realiza el inicio de sesión de un cliente
     *
     * @param request Datos de inicio de sesión
     * @return CustomerItem con token de autenticación si las credenciales son válidas
     * @throws IllegalArgumentException si las credenciales son inválidas
     */
    suspend fun login(request: LoginRequest): CustomerResponse.CustomerItem?

    /**
     * Obtiene todos los clientes registrados
     *
     * @return CustomerResponse conteniendo la lista de todos los clientes
     */
    suspend fun findAll(): CustomerResponse

    /**
     * Crea un nuevo cliente
     *
     * @param request Datos del cliente a crear
     * @return CustomerItem con los datos del cliente creado
     * @throws IllegalArgumentException si los datos son inválidos o el email ya existe
     */
    suspend fun create(request: CustomerCreationRequest): CustomerResponse.CustomerItem

    /**
     * Elimina un cliente por su ID
     *
     * @param id Identificador del cliente a eliminar
     * @throws NoSuchElementException si el cliente no existe
     */
    suspend fun delete(id: Long)

    /**
     * Actualiza los datos de un cliente
     *
     * @param id Identificador del cliente a actualizar
     * @param request Datos actualizados del cliente
     * @return CustomerItem con los datos actualizados
     * @throws NoSuchElementException si el cliente no existe
     */
    suspend fun update(id: Long, request: CustomerUpdateRequest): CustomerResponse.CustomerItem
    @Transactional(readOnly = true)
    suspend fun findByCountry(country: String): CustomerResponse
    @Transactional(readOnly = true)
    suspend fun findByPassportExpiryBefore(date: LocalDate): CustomerResponse
    @Transactional(readOnly = true)
    suspend fun getStatistics(): CustomerStatistics
}