package com.jmfg.certs.dh.prodev.model.dto

import com.jmfg.certs.dh.prodev.model.Category
import com.jmfg.certs.dh.prodev.model.Photo
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * Solicitud de creación de un nuevo alojamiento
 *
 * Esta clase representa los datos necesarios para registrar un nuevo
 * alojamiento en el sistema, incluyendo información detallada sobre
 * servicios, características y un conjunto completo de fotografías.
 *
 * @property name Nombre del alojamiento
 * @property address Dirección física
 * @property city Ciudad donde se ubica
 * @property country País donde se ubica
 * @property stars Calificación en estrellas (1-5)
 * @property averageCustomerRating Calificación promedio de clientes (1-10)
 * @property price Precio por noche
 * @property description Descripción detallada
 * @property category Categoría del alojamiento
 * @property availableFrom Fecha de inicio de disponibilidad
 * @property availableTo Fecha final de disponibilidad
 * @property maxOccupancy Capacidad máxima de personas
 * @property checkInTime Hora de entrada (formato HH:mm)
 * @property checkOutTime Hora de salida (formato HH:mm)
 * @property cancellationPolicy Política de cancelación
 * @property amenities Lista de comodidades disponibles
 * @property roomSizeSquareMeters Tamaño de la habitación en metros cuadrados
 * @property isPetFriendly Indica si se permiten mascotas
 * @property hasParking Indica si tiene estacionamiento disponible
 * @property photos Lista detallada de fotos del alojamiento
 * @property isFavorite Indica si es un alojamiento destacado
 */
data class LodgingCreationRequest(
    val name: String,
    val address: String,
    val city: String,
    val country: String,
    val stars: Int,
    val averageCustomerRating: Int,
    val price: Double,
    val description: String,
    val category: Category,
    val availableFrom: LocalDateTime,
    val availableTo: LocalDateTime,
    val maxOccupancy: Int,
    val checkInTime: String,
    val checkOutTime: String,
    val cancellationPolicy: String,
    val amenities: List<String>,
    val roomSizeSquareMeters: Double?,
    val isPetFriendly: Boolean,
    val hasParking: Boolean,
    val photos: List<PhotoRequest>,
    val isFavorite: Boolean = false
) {
    /**
     * Solicitud de creación de una foto para el alojamiento
     *
     * @property url URL de la imagen
     * @property photoType Tipo de foto (habitación, baño, etc.)
     * @property isMain Indica si es la foto principal
     * @property altText Texto alternativo para accesibilidad
     * @property description Descripción detallada de la foto
     * @property widthPx Ancho en píxeles
     * @property heightPx Alto en píxeles
     */
    data class PhotoRequest(
        val url: String,
        val photoType: Photo.PhotoType,
        val isMain: Boolean = false,
        val altText: String,
        val description: String = "",
        val widthPx: Int?,
        val heightPx: Int?,
    ) {
        init {
            require(url.isNotBlank()) { "La URL de la foto no puede estar vacía" }
            require(url.startsWith("http")) { "La URL debe comenzar con http" }
            require(altText.isNotBlank()) { "El texto alternativo no puede estar vacío" }
            widthPx?.let {
                require(it > 0) { "El ancho debe ser mayor a 0" }
            }
            heightPx?.let {
                require(it > 0) { "El alto debe ser mayor a 0" }
            }
        }
    }

    init {
        require(name.isNotBlank()) { "El nombre no puede estar vacío" }
        require(address.isNotBlank()) { "La dirección no puede estar vacía" }
        require(city.isNotBlank()) { "La ciudad no puede estar vacía" }
        require(country.isNotBlank()) { "El país no puede estar vacío" }
        require(stars in 1..5) { "Las estrellas deben estar entre 1 y 5" }
        require(averageCustomerRating in 1..10) { "La calificación debe estar entre 1 y 10" }
        require(price > 0) { "El precio debe ser mayor que 0" }
        require(description.isNotBlank()) { "La descripción no puede estar vacía" }
        require(availableFrom.isBefore(availableTo)) { "La fecha inicial debe ser anterior a la fecha final" }
        require(photos.isNotEmpty()) { "Debe incluir al menos una foto" }
        require(photos.count { it.isMain } == 1) { "Debe haber exactamente una foto principal" }
        require(maxOccupancy > 0) { "La ocupación máxima debe ser mayor a 0" }
        require(checkInTime.matches(Regex("^([01]?[0-9]|2[0-3]):[0-5][0-9]$"))) {
            "El formato de hora de check-in debe ser HH:mm"
        }
        require(checkOutTime.matches(Regex("^([01]?[0-9]|2[0-3]):[0-5][0-9]$"))) {
            "El formato de hora de check-out debe ser HH:mm"
        }
        require(cancellationPolicy.isNotBlank()) { "La política de cancelación no puede estar vacía" }
        require(amenities.isNotEmpty()) { "Debe incluir al menos una amenidad" }
        require(amenities.all { it.isNotBlank() }) { "Las amenidades no pueden estar vacías" }
        roomSizeSquareMeters?.let {
            require(it > 0) { "El tamaño de la habitación debe ser mayor a 0" }
        }

        require(photos.any { it.photoType == Photo.PhotoType.ROOM }) {
            "Debe incluir al menos una foto de la habitación"
        }
        require(photos.map { it.url }.distinct().size == photos.size) {
            "No pueden existir URLs duplicadas"
        }
    }


    /**
     * Obtiene las fotos de un tipo específico
     */
    fun getPhotosByType(type: Photo.PhotoType): List<PhotoRequest> =
        photos.filter { it.photoType == type }

    /**
     * Obtiene la foto principal
     */
    fun getMainPhoto(): PhotoRequest? =
        photos.find { it.isMain }


}

/**
 * Solicitud de búsqueda de alojamientos
 *
 * @property destination Destino (ciudad) a buscar
 * @property checkIn Fecha de entrada
 * @property checkOut Fecha de salida
 */
data class LodgingSearchRequest(
    val destination: String,
    val checkIn: LocalDate,
    val checkOut: LocalDate,
    val guests: Int = 1,
    val priceRange: ClosedRange<Double>? = null
) {
    init {
        require(destination.isNotBlank()) { "El destino no puede estar vacío" }
        require(checkIn.isBefore(checkOut)) { "La fecha de entrada debe ser anterior a la de salida" }
        require(!checkIn.isBefore(LocalDate.now())) { "La fecha de entrada no puede ser en el pasado" }
    }

    /**
     * Calcula la duración de la estadía en noches
     */
    val stayDuration: Long
        get() = ChronoUnit.DAYS.between(checkIn, checkOut)

    companion object {
        /**
         * Crea una búsqueda para fechas específicas
         *
         * @param destination Ciudad destino
         * @param daysFromNow Días a partir de hoy para check-in
         * @param stayDuration Duración de la estadía en días
         */
        fun forDates(
            destination: String,
            daysFromNow: Long = 1,
            stayDuration: Long = 1
        ): LodgingSearchRequest {
            val checkIn = LocalDate.now().plusDays(daysFromNow)
            return LodgingSearchRequest(
                destination = destination,
                checkIn = checkIn,
                checkOut = checkIn.plusDays(stayDuration)
            )
        }
    }
}


/**
 * Respuesta que contiene información de alojamientos
 *
 * @property lodgings Lista de alojamientos con sus detalles
 */
data class LodgingResponse(
    val lodgings: List<LodgingItem>
) {
    /**
     * Representa los datos de un alojamiento individual
     */
    data class LodgingItem(
        val id: String,
        val name: String,
        val address: String,
        val city: String,
        val country: String,
        val price: Double,
        val distanceFromDownTown: Double,
        val stars: Int,
        val averageCustomerRating: Int,
        val grade: Rating,
        val description: String,
        val category: String,
        val availableFrom: LocalDateTime,
        val availableTo: LocalDateTime,
        val isFavorite: Boolean,
        val photos: List<String>,
        val displayPhoto: String,
        val amenities: List<String>,
        val cancellationPolicy: String,
        val roomSizeSquareMeters: Double,
        val isPetFriendly: Boolean,
        val hasParking: Boolean,
        val mainPhoto: Photo,
        val maxOccupancy: Int,
        val checkInTime: String = "15:00",
        val checkOutTime: String = "11:00",
    ) {
        /**
         * Calcula si el alojamiento está actualmente disponible
         */
        val isCurrentlyAvailable: Boolean
            get() = LocalDateTime.now().run {
                isAfter(availableFrom) && isBefore(availableTo)
            }

        /**
         * Calcula si el alojamiento es de alta categoría
         */
        val isLuxury: Boolean
            get() = stars >= 4 && averageCustomerRating >= 4

        /**
         * Obtiene la ubicación completa
         */
        val fullLocation: String
            get() = "$address, $city, $country"
    }


    /**
     * Verifica si hay alojamientos disponibles
     */
    fun hasAvailableLodgings(): Boolean =
        lodgings.any { it.isCurrentlyAvailable }

    /**
     * Filtra alojamientos por rango de precio
     */
    fun filterByPriceRange(minPrice: Double, maxPrice: Double): List<LodgingItem> =
        lodgings.filter { it.price in minPrice..maxPrice }

    /**
     * Obtiene alojamientos ordenados por calificación
     */
    fun getTopRated(limit: Int = 10): List<LodgingItem> =
        lodgings
            .sortedByDescending { it.averageCustomerRating }
            .take(limit)

    /**
     * Agrupa alojamientos por ciudad
     */
    fun groupByCity(): Map<String, List<LodgingItem>> =
        lodgings.groupBy { it.city }

    /**
     * Calcula el precio promedio por ciudad
     */
    fun getAveragePriceByCity(): Map<String, Double> =
        groupByCity().mapValues { (_, lodgings) ->
            lodgings.map { it.price }.average()
        }
}

/**
 * Enumeración que representa los posibles grados de calificación
 * en el sistema.
 */
enum class Rating(val description: String, val range: IntRange) {
    PESIMO("Pésimo", 0..3),
    MALO("Malo", 4..5),
    REGULAR("Regular", 6..7),
    BUENO("Bueno", 7..8),
    MUY_BUENO("Muy bueno", 8..9),
    EXCELENTE("Excelente", 9..10);

    companion object {
        fun fromScore(score: Int): Rating {
            require(score in 0..10) { "La calificación debe estar entre 0 y 10" }
            return entries.first { score in it.range }
        }
    }
}
