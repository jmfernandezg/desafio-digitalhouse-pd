package com.jmfg.certs.dh.prodev.app.service

import com.jmfg.certs.dh.prodev.app.repository.LodgingRepository
import com.jmfg.certs.dh.prodev.app.repository.PhotoRepository
import com.jmfg.certs.dh.prodev.model.Category
import com.jmfg.certs.dh.prodev.model.Lodging
import com.jmfg.certs.dh.prodev.model.Photo
import com.jmfg.certs.dh.prodev.model.dto.*
import com.jmfg.certs.dh.prodev.service.LodgingService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Implementación del servicio de gestión de alojamientos
 *
 * @property lodgingRepository Repositorio para operaciones con alojamientos
 * @property photoRepository Repositorio para operaciones con fotos
 */
@Service
class LodgingServiceImpl(
    private val lodgingRepository: LodgingRepository,
    private val photoRepository: PhotoRepository
) : LodgingService {

    /**
     * Busca un alojamiento por su ID
     */
    @Transactional(readOnly = true)
    override suspend fun findById(id: String): LodgingResponse.LodgingItem? {
        val numericId = id.toLongOrNull() ?: throw IllegalArgumentException("ID inválido")
        return lodgingRepository.findByIdOrNull(numericId)?.toLodgingDto()
    }

    /**
     * Obtiene todos los alojamientos
     */
    @Transactional(readOnly = true)
    override suspend fun findAll(): LodgingResponse {
        val lodgings = lodgingRepository.findAll()
        return createLodgingResponse(lodgings.map { it.toLodgingDto() })
    }

    /**
     * Crea un nuevo alojamiento
     */
    @Transactional
    override suspend fun create(request: LodgingCreationRequest): LodgingResponse.LodgingItem {
        // Crear el alojamiento
        val lodging = Lodging(
            name = request.name,
            address = request.address,
            city = request.city,
            country = request.country,
            price = request.price,
            stars = request.stars,
            averageCustomerRating = request.averageCustomerRating,
            description = request.description,
            category = request.category,
            availableFrom = request.availableFrom,
            availableTo = request.availableTo,
            maxOccupancy = request.maxOccupancy,
            checkInTime = request.checkInTime,
            checkOutTime = request.checkOutTime,
            cancellationPolicy = request.cancellationPolicy,
            amenities = request.amenities.joinToString(","),
            roomSizeSquareMeters = request.roomSizeSquareMeters,
            isPetFriendly = request.isPetFriendly,
            hasParking = request.hasParking,
            isFavorite = request.isFavorite
        )

        val savedLodging = lodgingRepository.save(lodging)

        // Crear y guardar las fotos
        val photos = request.photos.map { photoRequest ->
            Photo(
                url = photoRequest.url,
                fileName = photoRequest.fileName,
                photoType = photoRequest.photoType,
                isMain = photoRequest.isMain,
                altText = photoRequest.altText,
                widthPx = photoRequest.widthPx,
                heightPx = photoRequest.heightPx,
                lodging = savedLodging
            )
        }
        photoRepository.saveAll(photos)

        return savedLodging.toLodgingDto()
    }

    /**
     * Elimina un alojamiento
     */
    @Transactional
    override suspend fun delete(id: String) {
        val numericId = id.toLongOrNull() ?: throw IllegalArgumentException("ID inválido")
        val lodging = lodgingRepository.findByIdOrNull(numericId)
            ?: throw LodgingNotFoundException("Alojamiento no encontrado")

        // Verificar si tiene reservas activas
        if (hasActiveBookings(lodging)) {
            throw IllegalStateException("No se puede eliminar un alojamiento con reservas activas")
        }

        lodgingRepository.delete(lodging)
    }

    /**
     * Actualiza un alojamiento existente
     */
    @Transactional
    override suspend fun update(lodging: Lodging): LodgingResponse.LodgingItem {
        if (!lodgingRepository.existsById(lodging.id)) {
            throw LodgingNotFoundException("Alojamiento no encontrado")
        }
        return lodgingRepository.save(lodging).toLodgingDto()
    }

    /**
     * Obtiene todas las categorías disponibles
     */
    @Transactional(readOnly = true)
    override suspend fun findAllCategories(): CategoryResponse {
        return CategoryResponse(
            categories = Category.entries.map {
                CategoryResponse.CategoryItem(
                    name = it.name,
                    displayName = it.toDisplayString(),
                    count = lodgingRepository.countByCategory(it)
                )
            }
        )
    }

    /**
     * Busca alojamientos por categoría
     */
    @Transactional(readOnly = true)
    override suspend fun findByCategory(category: Category): LodgingResponse {
        val lodgings = lodgingRepository.findByCategory(category)
        return createLodgingResponse(lodgings.map { it.toLodgingDto() })
    }

    /**
     * Obtiene todas las ciudades disponibles
     */
    @Transactional(readOnly = true)
    override suspend fun findAllCities(): Set<String> {
        return lodgingRepository.findDistinctCities()
    }

    /**
     * Realiza una búsqueda avanzada de alojamientos
     */
    @Transactional(readOnly = true)
    override suspend fun search(request: LodgingSearchRequest): LodgingResponse {
        val lodgings = lodgingRepository.findAll().filter { lodging ->
            matchesSearchCriteria(lodging, request)
        }

        return createLodgingResponse(
            lodgings = lodgings.map { it.toLodgingDto() }
        )
    }

    /**
     * Verifica si un alojamiento coincide con los criterios de búsqueda
     */
    private fun matchesSearchCriteria(lodging: Lodging, request: LodgingSearchRequest): Boolean {
        return lodging.city.equals(request.destination, ignoreCase = true) &&
                lodging.isAvailableOn(request.checkIn.atStartOfDay()) &&
                lodging.isAvailableOn(request.checkOut.minusDays(1).atStartOfDay()) &&
                lodging.maxOccupancy >= request.guests &&
                matchesFilters(lodging, request)
    }

    /**
     * Verifica si un alojamiento cumple con los filtros adicionales
     */
    private fun matchesFilters(lodging: Lodging, request: LodgingSearchRequest): Boolean {
        val filters = request.filters

        return (filters.categories.isEmpty() || lodging.category in filters.categories) &&
                (filters.minStars == null || lodging.stars >= filters.minStars) &&
                (filters.minRating == null || lodging.averageCustomerRating >= filters.minRating) &&
                (filters.isPetFriendly == null || lodging.isPetFriendly == filters.isPetFriendly) &&
                (filters.hasParking == null || lodging.hasParking == filters.hasParking) &&
                (filters.minRoomSize == null ||
                        (lodging.roomSizeSquareMeters?.let { it >= filters.minRoomSize } ?: false)) &&
                (request.priceRange == null ||
                        lodging.price in request.priceRange.start..request.priceRange.endInclusive) &&
                matchesAmenities(lodging, filters.amenities)
    }

    /**
     * Verifica si un alojamiento tiene las amenidades requeridas
     */
    private fun matchesAmenities(lodging: Lodging, requiredAmenities: Set<String>): Boolean {
        if (requiredAmenities.isEmpty()) return true
        val lodgingAmenities = lodging.getAmenitiesList()
        return requiredAmenities.all { required ->
            lodgingAmenities.any { it.equals(required, ignoreCase = true) }
        }
    }

    /**
     * Verifica si un alojamiento tiene reservas activas
     */
    private fun hasActiveBookings(lodging: Lodging): Boolean {
        // Implementar lógica de verificación de reservas
        return false // Placeholder
    }

    /**
     * Crea una respuesta completa de alojamientos con estadísticas
     */
    private fun createLodgingResponse(lodgings: List<LodgingResponse.LodgingItem>): LodgingResponse {
        val prices = lodgings.map { it.price }

        return LodgingResponse(
            lodgings = lodgings,
            statistics = LodgingResponse.SearchStatistics(
                averagePrice = prices.average(),
                priceRange = LodgingSearchRequest.PriceRange(
                    min = prices.minOrNull() ?: 0.0,
                    max = prices.maxOrNull() ?: 0.0
                ),
                categoryDistribution = lodgings.groupBy { it.category }
                    .mapValues { it.value.size },
                averageRating = lodgings.map { it.averageCustomerRating }.average()
            )
        )
    }
}

class LodgingNotFoundException(s: String) : Throwable() {

}
