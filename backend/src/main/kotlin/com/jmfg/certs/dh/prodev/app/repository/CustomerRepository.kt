package com.jmfg.certs.dh.prodev.app.repository

import com.jmfg.certs.dh.prodev.model.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

/**
 * Repositorio para la gestión de clientes en la base de datos
 *
 * Este repositorio proporciona operaciones de acceso a datos para la entidad Cliente.
 * Extiende JpaRepository para heredar las operaciones básicas de CRUD y añade
 * métodos personalizados para consultas específicas del negocio.
 */
@Repository
interface CustomerRepository : JpaRepository<Customer, Long> {

    /**
     * Busca un cliente por su correo electrónico
     *
     * @param email Correo electrónico del cliente a buscar
     * @return Cliente encontrado o null si no existe
     */
    fun findByEmail(email: String): Customer?

    /**
     * Busca clientes por su nombre o apellido
     *
     * @param name Nombre o apellido a buscar
     * @return Lista de clientes que coinciden con el criterio de búsqueda
     */
    @Query(
        """
        SELECT c FROM Customer c 
        WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :name, '%')) 
        OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :name, '%'))
        """
    )
    fun findCustomerByName(@Param("name") name: String): List<Customer>

    /**
     * Verifica si existe un cliente con el correo electrónico especificado
     *
     * @param email Correo electrónico a verificar
     * @return true si existe un cliente con ese email, false en caso contrario
     */
    fun existsByEmail(email: String): Boolean

    /**
     * Busca clientes por país de residencia
     *
     * @param country País de residencia a buscar
     * @return Lista de clientes del país especificado
     */
    fun findByCountryOfResidence(country: String): List<Customer>

    /**
     * Busca clientes cuyo pasaporte expire antes de la fecha especificada
     *
     * @param date Fecha límite de vencimiento
     * @return Lista de clientes con pasaportes próximos a vencer
     */
    fun findByPassportExpiryBefore(date: java.time.LocalDate): List<Customer>
}