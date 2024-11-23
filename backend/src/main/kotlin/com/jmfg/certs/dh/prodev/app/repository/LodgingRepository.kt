package com.jmfg.certs.dh.prodev.app.repository

import com.jmfg.certs.dh.prodev.model.Category
import com.jmfg.certs.dh.prodev.model.Lodging
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

/**
 * Repositorio para la gestión de alojamientos en la base de datos
 *
 * Este repositorio proporciona operaciones de acceso a datos para la entidad Alojamiento.
 * Incluye consultas personalizadas para búsquedas específicas y utiliza EntityGraph
 * para optimizar la carga de relaciones (como fotos).
 *
 * @property T Lodging - Tipo de entidad que maneja el repositorio
 * @property ID String - Tipo de dato del identificador único del alojamiento
 */
@Repository
interface LodgingRepository : JpaRepository<Lodging, String> {

    /**
     * Busca alojamientos por categoría, incluyendo sus fotos
     *
     * @param category Categoría de alojamiento a buscar
     * @return Lista de alojamientos de la categoría especificada
     */
    @EntityGraph(attributePaths = ["photos"])
    fun findByCategory(category: Category): List<Lodging>

    /**
     * Obtiene todos los alojamientos con sus fotos
     *
     * @return Lista completa de alojamientos
     */
    @EntityGraph(attributePaths = ["photos"])
    override fun findAll(): List<Lodging>

    /**
     * Obtiene todos los alojamientos paginados
     *
     * @param pageable Configuración de paginación
     * @return Página de alojamientos
     */
    @EntityGraph(attributePaths = ["photos"])
    override fun findAll(pageable: Pageable): Page<Lodging>

    /**
     * Recupera todas las categorías disponibles
     *
     * @return Lista de categorías únicas
     */
    @Query("SELECT DISTINCT l.category FROM Lodging l")
    fun findAllCategories(): List<Category>

    /**
     * Obtiene todas las ciudades con alojamientos
     *
     * @return Conjunto de locaciones en formato "ciudad, país"
     */
    @Query("SELECT DISTINCT CONCAT(l.city, ', ', l.country) FROM Lodging l")
    fun findAllCities(): Set<String>

    /**
     * Busca alojamientos por ubicación y fechas de disponibilidad
     *
     * @param location Texto de búsqueda para ciudad o país
     * @param checkIn Fecha de inicio de la estadía
     * @param checkOut Fecha de fin de la estadía
     * @return Lista de alojamientos disponibles
     */
    @Query(
        """
        SELECT l FROM Lodging l 
        WHERE (:location IS NULL OR 
              LOWER(l.city) LIKE LOWER(CONCAT('%', :location, '%')) OR 
              LOWER(l.country) LIKE LOWER(CONCAT('%', :location, '%')))
        AND l.availableFrom <= :checkIn 
        AND l.availableTo >= :checkOut
    """
    )
    @EntityGraph(attributePaths = ["photos"])
    fun search(
        @Param("location") location: String?,
        @Param("checkIn") checkIn: LocalDateTime,
        @Param("checkOut") checkOut: LocalDateTime
    ): List<Lodging>


}