package com.jmfg.certs.dh.prodev.app.service

import com.jmfg.certs.dh.prodev.app.repository.LodgingRepository
import com.jmfg.certs.dh.prodev.model.Category
import com.jmfg.certs.dh.prodev.model.Lodging
import com.jmfg.certs.dh.prodev.model.dto.CategoryResponse
import com.jmfg.certs.dh.prodev.model.dto.LodgingCreationRequest
import com.jmfg.certs.dh.prodev.model.dto.LodgingResponse
import com.jmfg.certs.dh.prodev.model.dto.LodgingResponse.LodgingItem
import com.jmfg.certs.dh.prodev.model.dto.LodgingSearchRequest
import com.jmfg.certs.dh.prodev.model.toLodgingDto
import com.jmfg.certs.dh.prodev.service.LodgingService
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

/**
 * Implementación del servicio de gestión de alojamientos
 *
 * Esta clase implementa la lógica de negocio para todas las operaciones
 * relacionadas con alojamientos, incluyendo:
 * - Gestión de alojamientos (CRUD)
 * - Búsqueda y filtrado
 * - Gestión de categorías
 * - Manejo de disponibilidad
 */
@Service
class LodgingServiceImpl(
    private val lodgingRepository: LodgingRepository
) : LodgingService {

    private val logger = LoggerFactory.getLogger(LodgingServiceImpl::class.java)

    /**
     * Crea un nuevo alojamiento
     *
     * @param request Datos del nuevo alojamiento
     * @return Alojamiento creado
     * @throws IllegalArgumentException si los datos son inválidos
     */
    @Transactional
    override fun create(request: LodgingCreationRequest): LodgingItem {
        logger.debug("Creando nuevo alojamiento: ${request.name}")

        validarDatosAlojamiento(request)

        return Lodging(
            name = request.name,
            description = request.description,
            category = request.category,
            price = request.price,
            address = request.address,
            city = request.city,
            country = request.country,
            stars = request.stars,
            averageCustomerRating = request.averageCustomerRating,
            availableFrom = request.availableFrom,
            availableTo = request.availableTo
        ).run {
            lodgingRepository.save(this)
        }.toLodgingDto().also {
            logger.info("Alojamiento creado exitosamente: ${request.name}")
        }
    }

    /**
     * Actualiza un alojamiento existente
     *
     * @param lodging Nuevos datos del alojamiento
     * @return Alojamiento actualizado
     * @throws NoSuchElementException si el alojamiento no existe
     */
    @Transactional
    override suspend fun update(lodging: Lodging): LodgingItem {
        logger.debug("Actualizando alojamiento con ID: ${lodging.id}")

        if (!lodgingRepository.existsById(lodging.id)) {
            logger.error("Intento de actualizar alojamiento inexistente: ${lodging.id}")
            throw NoSuchElementException("Alojamiento no encontrado con ID: ${lodging.id}")
        }

        return lodging.run {
            lodgingRepository.save(this)
        }.toLodgingDto().also {
            logger.info("Alojamiento actualizado exitosamente: ${lodging.id}")
        }
    }

    /**
     * Busca un alojamiento por su identificador
     *
     * @param id Identificador del alojamiento
     * @return Alojamiento encontrado o null si no existe
     */
    @Transactional(readOnly = true)
    override suspend fun findById(id: String): LodgingItem? {
        logger.debug("Buscando alojamiento con ID: $id")
        return lodgingRepository.findByIdOrNull(id)?.toLodgingDto()
    }

    /**
     * Obtiene todos los alojamientos disponibles
     *
     * @return Lista de todos los alojamientos
     */
    @Transactional(readOnly = true)
    override suspend fun findAll(): LodgingResponse {
        logger.debug("Consultando todos los alojamientos")
        return lodgingRepository.findAll()
            .map { it.toLodgingDto() }
            .let { LodgingResponse(it) }
    }

    /**
     * Obtiene todas las categorías con sus estadísticas
     *
     * @return Respuesta con información de categorías
     */
    @Transactional(readOnly = true)
    override suspend fun findAllCategories(): CategoryResponse {
        logger.debug("Consultando todas las categorías")
        return lodgingRepository.findAllCategories().map { category ->
            val lodgings = lodgingRepository.findByCategory(category)
            CategoryResponse.CategoryItem(
                name = category.toCapitalizedString(),
                imageUrl = lodgings.flatMap { it.photos }
                    .map { it.url }
                    .firstOrNull() ?: "default-image.jpg",
                numberOfLodgings = lodgings.size
            )
        }.let { CategoryResponse(it) }
    }

    /**
     * Obtiene todas las ciudades disponibles
     *
     * @return Conjunto de ciudades con alojamientos
     */
    @Transactional(readOnly = true)
    override suspend fun findAllCities(): Set<String> {
        logger.debug("Consultando todas las ciudades")
        return lodgingRepository.findAllCities()
    }


    @Transactional(readOnly = true)
    override suspend fun search(request: LodgingSearchRequest): LodgingResponse {
        logger.debug("Buscando alojamientos con criterios: {}", request)

        validarCriteriosBusqueda(request)

        return lodgingRepository.search(
            request.destination,
            request.checkIn.atStartOfDay(),
            request.checkOut.atStartOfDay()
        ).map { it.toLodgingDto() }
            .let { LodgingResponse(it) }
    }


    /**
     * Busca alojamientos por categoría
     *
     * @param category Categoría a buscar
     * @return Lista de alojamientos de la categoría
     */
    @Transactional(readOnly = true)
    override suspend fun findByCategory(category: Category): LodgingResponse {
        logger.debug("Buscando alojamientos por categoría: {}", category)
        return lodgingRepository.findByCategory(category)
            .map { it.toLodgingDto() }
            .let { LodgingResponse(it) }
    }

    /**
     * Elimina un alojamiento
     *
     * @param id Identificador del alojamiento a eliminar
     * @throws NoSuchElementException si el alojamiento no existe
     */
    @Transactional
    override suspend fun delete(id: String) {
        logger.debug("Eliminando alojamiento con ID: $id")

        if (!lodgingRepository.existsById(id)) {
            logger.error("Intento de eliminar alojamiento inexistente: $id")
            throw NoSuchElementException("Alojamiento no encontrado con ID: $id")
        }

        lodgingRepository.deleteById(id)
        logger.info("Alojamiento eliminado exitosamente: $id")
    }

    /**
     * Válida los datos de creación de un alojamiento
     *
     * @param request Datos a validar
     * @throws IllegalArgumentException si los datos son inválidos
     */
    private fun validarDatosAlojamiento(request: LodgingCreationRequest) {
        if (request.price <= 0) {
            throw IllegalArgumentException("El precio debe ser mayor a 0")
        }

        if (request.stars !in 1..5) {
            throw IllegalArgumentException("Las estrellas deben estar entre 1 y 5")
        }

        if (request.availableFrom >= request.availableTo) {
            throw IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha de fin")
        }
    }

    /**
     * Válida los criterios de búsqueda
     *
     * @param request Criterios a validar
     * @throws IllegalArgumentException si los criterios son inválidos
     */
    private fun validarCriteriosBusqueda(request: LodgingSearchRequest) {
        if (request.checkIn.isAfter(request.checkOut)) {
            throw IllegalArgumentException("La fecha de entrada debe ser anterior a la fecha de salida")
        }

        if (request.checkIn.isBefore(LocalDateTime.now().toLocalDate())) {
            throw IllegalArgumentException("La fecha de entrada no puede ser anterior a hoy")
        }
    }
}