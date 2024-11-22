package com.jmfg.certs.dh.prodev.service

import com.jmfg.certs.dh.prodev.model.Category
import com.jmfg.certs.dh.prodev.model.Lodging
import com.jmfg.certs.dh.prodev.model.dto.CategoryResponse
import com.jmfg.certs.dh.prodev.model.dto.LodgingCreationRequest
import com.jmfg.certs.dh.prodev.model.dto.LodgingResponse
import com.jmfg.certs.dh.prodev.model.dto.LodgingResponse.LodgingItem
import com.jmfg.certs.dh.prodev.model.dto.LodgingSearchRequest

/**
 * Servicio que gestiona las operaciones relacionadas con alojamientos.
 * Proporciona funcionalidades para la búsqueda, creación, actualización
 * y gestión de alojamientos y sus categorías en el sistema.
 */
interface LodgingService {
    /**
     * Busca un alojamiento específico por su identificador.
     *
     * @param id Identificador único del alojamiento
     * @return LodgingItem si se encuentra el alojamiento, null si no existe
     * @throws IllegalArgumentException si el ID proporcionado no es válido
     */
    suspend fun findById(id: String): LodgingItem?

    /**
     * Obtiene todos los alojamientos registrados en el sistema.
     *
     * @return Lista completa de alojamientos encapsulada en LodgingResponse
     * @throws ServiceException si hay un error al recuperar los datos
     */
    suspend fun findAll(): LodgingResponse

    /**
     * Crea un nuevo alojamiento en el sistema.
     *
     * @param request Datos necesarios para la creación del alojamiento
     * @return LodgingItem con los datos del alojamiento creado
     * @throws IllegalArgumentException si los datos de creación son inválidos
     * @throws DuplicateLodgingException si ya existe un alojamiento con los mismos datos únicos
     */
    suspend fun create(request: LodgingCreationRequest): LodgingItem

    /**
     * Elimina un alojamiento del sistema por su identificador.
     *
     * @param id Identificador único del alojamiento a eliminar
     * @throws ResourceNotFoundException si el alojamiento no existe
     * @throws IllegalStateException si el alojamiento no puede ser eliminado
     */
    suspend fun delete(id: String)

    /**
     * Actualiza los datos de un alojamiento existente.
     *
     * @param lodging Datos actualizados del alojamiento
     * @return LodgingItem con los datos actualizados
     * @throws ResourceNotFoundException si el alojamiento no existe
     * @throws IllegalArgumentException si los datos de actualización son inválidos
     */
    suspend fun update(lodging: Lodging): LodgingItem

    /**
     * Obtiene todas las categorías de alojamientos disponibles.
     *
     * @return Lista de categorías encapsulada en CategoryResponse
     * @throws ServiceException si hay un error al recuperar las categorías
     */
    suspend fun findAllCategories(): CategoryResponse

    /**
     * Busca alojamientos por categoría específica.
     *
     * @param category Categoría por la cual filtrar los alojamientos
     * @return Lista de alojamientos filtrados por categoría
     * @throws IllegalArgumentException si la categoría proporcionada no es válida
     */
    suspend fun findByCategory(category: Category): LodgingResponse

    /**
     * Obtiene la lista de todas las ciudades donde hay alojamientos disponibles.
     *
     * @return Conjunto de nombres de ciudades sin duplicados
     * @throws ServiceException si hay un error al recuperar las ciudades
     */
    suspend fun findAllCities(): Set<String>

    /**
     * Realiza una búsqueda avanzada de alojamientos según criterios específicos.
     *
     * @param request Criterios de búsqueda para filtrar alojamientos
     * @return Lista de alojamientos que cumplen con los criterios de búsqueda
     * @throws IllegalArgumentException si los criterios de búsqueda son inválidos
     */
    suspend fun search(request: LodgingSearchRequest): LodgingResponse
}