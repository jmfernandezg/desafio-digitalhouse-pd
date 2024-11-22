package com.jmfg.certs.dh.prodev.model.dto

import com.jmfg.certs.dh.prodev.model.Category
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * Solicitud de creación de un nuevo alojamiento
 *
 * Esta clase representa los datos necesarios para registrar un nuevo
 * alojamiento en el sistema.
 *
 * @property name Nombre del alojamiento
 * @property address Dirección física
 * @property city Ciudad donde se ubica
 * @property country País donde se ubica
 * @property stars Calificación en estrellas (1-5)
 * @property averageCustomerRating Calificación promedio de clientes (1-5)
 * @property price Precio por noche
 * @property description Descripción detallada
 * @property category Categoría del alojamiento
 * @property availableFrom Fecha de inicio de disponibilidad
 * @property availableTo Fecha final de disponibilidad
 * @property photos Lista de URLs de fotos
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
    val photos: List<String>,
    val isFavorite: Boolean = false
) {
    init {
        require(name.isNotBlank()) { "El nombre no puede estar vacío" }
        require(address.isNotBlank()) { "La dirección no puede estar vacía" }
        require(city.isNotBlank()) { "La ciudad no puede estar vacía" }
        require(country.isNotBlank()) { "El país no puede estar vacío" }
        require(stars in 1..5) { "Las estrellas deben estar entre 1 y 5" }
        require(averageCustomerRating in 1..5) { "La calificación debe estar entre 1 y 5" }
        require(price > 0) { "El precio debe ser mayor que 0" }
        require(description.isNotBlank()) { "La descripción no puede estar vacía" }
        require(availableFrom.isBefore(availableTo)) { "La fecha inicial debe ser anterior a la fecha final" }
        require(photos.isNotEmpty()) { "Debe incluir al menos una foto" }
        require(photos.all { it.startsWith("http") }) { "Todas las URLs de fotos deben ser válidas" }
    }

    companion object {
        /**
         * Crea una instancia de prueba
         *
         * @param category Categoría del alojamiento
         * @return LodgingCreationRequest con datos de prueba
         */
        fun createTestInstance(category: Category = Category.HOTEL) = LodgingCreationRequest(
            name = "Test Lodging",
            address = "Test Address 123",
            city = "Test City",
            country = "Test Country",
            stars = 4,
            averageCustomerRating = 4,
            price = 100.0,
            description = "Test Description",
            category = category,
            availableFrom = LocalDateTime.now(),
            availableTo = LocalDateTime.now().plusMonths(1),
            photos = listOf("https://example.com/photo1.jpg")
        )
    }
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
    val checkOut: LocalDate
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
        val grade: String,
        val description: String,
        val category: String,
        val availableFrom: LocalDateTime,
        val availableTo: LocalDateTime,
        val isFavorite: Boolean,
        val photos: List<String>,
        val displayPhoto: String
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

    companion object {
        /**
         * Crea una respuesta vacía
         */
        fun empty() = LodgingResponse(emptyList())

        /**
         * Crea una respuesta con un solo alojamiento
         */
        fun single(lodging: LodgingItem) = LodgingResponse(listOf(lodging))
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