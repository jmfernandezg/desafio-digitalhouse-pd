package com.jmfg.certs.dh.prodev.service

import com.jmfg.certs.dh.prodev.model.Customer
import com.jmfg.certs.dh.prodev.model.dto.CustomerCreationRequest
import com.jmfg.certs.dh.prodev.model.dto.CustomerResponse
import com.jmfg.certs.dh.prodev.model.dto.LoginRequest

/**
 * Servicio que gestiona las operaciones relacionadas con los clientes.
 * Proporciona funcionalidades para la autenticación, gestión y manipulación
 * de datos de clientes en el sistema.
 */
interface CustomerService {
    /**
     * Autentica un cliente en el sistema.
     *
     * @param request Datos de inicio de sesión del cliente
     * @return CustomerItem si la autenticación es exitosa, null en caso contrario
     * @throws IllegalArgumentException si los datos de inicio de sesión son inválidos
     */
    suspend fun login(request: LoginRequest): CustomerResponse.CustomerItem?

    /**
     * Obtiene todos los clientes registrados en el sistema.
     *
     * @return Lista completa de clientes encapsulada en CustomerResponse
     * @throws ServiceException si hay un error al recuperar los datos
     */
    suspend fun findAll(): CustomerResponse

    /**
     * Crea un nuevo cliente en el sistema.
     *
     * @param request Datos necesarios para la creación del cliente
     * @return CustomerItem con los datos del cliente creado, null si la creación falla
     * @throws IllegalArgumentException si los datos de creación son inválidos
     * @throws DuplicateCustomerException si el cliente ya existe
     */
    suspend fun create(request: CustomerCreationRequest): CustomerResponse.CustomerItem?

    /**
     * Elimina un cliente del sistema por su identificador.
     *
     * @param id Identificador único del cliente
     * @throws ResourceNotFoundException si el cliente no existe
     * @throws IllegalStateException si el cliente no puede ser eliminado
     */
    suspend fun delete(id: String)

    /**
     * Actualiza los datos de un cliente existente.
     *
     * @param customer Datos actualizados del cliente
     * @return CustomerItem con los datos actualizados
     * @throws ResourceNotFoundException si el cliente no existe
     * @throws IllegalArgumentException si los datos de actualización son inválidos
     */
    suspend fun update(customer: Customer): CustomerResponse.CustomerItem
}