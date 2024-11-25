package com.jmfg.certs.dh.prodev.model

import com.jmfg.certs.dh.prodev.Util
import com.jmfg.certs.dh.prodev.model.dto.LodgingResponse
import jakarta.persistence.*
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * Categorías disponibles de alojamiento
 */
enum class Category {
    HOTEL,
    HOSTEL,
    DEPARTMENT,
    BED_AND_BREAKFAST;     // Nueva categoría añadida

    /**
     * Convierte la categoría a un string capitalizado para mostrar
     */
    fun toDisplayString(): String = Util.toCapitalizedString(name)
}

/**
 * Entidad principal que representa un alojamiento en el sistema
 *
 * Gestiona toda la información relacionada con las propiedades disponibles
 * para reserva, incluyendo sus características, disponibilidad y fotos.
 *
 * Mejoras implementadas:
 * - Identificador numérico para mejor rendimiento
 * - Campos adicionales para información relevante de agencias de viajes
 * - Validaciones mejoradas
 * - Índices optimizados
 * - Métodos de utilidad adicionales
 */
@Entity
@Table(
    name = "lodgings",
    indexes = [
        Index(name = "idx_lodging_city", columnList = "city"),
        Index(name = "idx_lodging_category", columnList = "category"),
        Index(name = "idx_lodging_price", columnList = "price"),
        Index(name = "idx_lodging_rating", columnList = "customer_rating"),
        Index(name = "idx_lodging_available_dates", columnList = "available_from,available_to")
    ]
)
data class Lodging(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String = "",

    @Column(nullable = false)
    val address: String = "",

    @Column(nullable = false)
    val city: String = "",

    @Column(nullable = false)
    val country: String = "",

    @Column(nullable = false)
    val price: Double = 0.0,

    @Column(nullable = false)
    val stars: Int = 3,

    @Column(name = "customer_rating", nullable = false)
    val averageCustomerRating: Int = 7,

    @Column(columnDefinition = "TEXT")
    val description: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val category: Category = Category.HOTEL,

    @Column(name = "available_from", nullable = false)
    val availableFrom: LocalDateTime = LocalDateTime.now(),

    @Column(name = "available_to", nullable = false)
    val availableTo: LocalDateTime = LocalDateTime.now(),

    @Column(name = "is_favorite")
    val isFavorite: Boolean = false,

    @Column(name = "max_occupancy", nullable = false)
    val maxOccupancy: Int = 2,

    @Column(name = "checkin_time")
    val checkInTime: String = "15:00",

    @Column(name = "checkout_time")
    val checkOutTime: String = "11:00",

    @Column(name = "cancellation_policy", columnDefinition = "TEXT")
    val cancellationPolicy: String = "",

    @Column(name = "amenities", columnDefinition = "TEXT")
    val amenities: String = "",

    @Column(name = "room_size_sqm")
    val roomSizeSquareMeters: Double? = null,

    @Column(name = "is_pet_friendly")
    val isPetFriendly: Boolean = false,

    @Column(name = "has_parking")
    val hasParking: Boolean = false,

    @OneToMany(
        mappedBy = "lodging",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY,
        orphanRemoval = true
    )
    val photos: List<Photo> = mutableListOf()
) : BaseEntity() {

    init {
        require(name.isNotBlank()) { "El nombre no puede estar vacío" }
        require(address.isNotBlank()) { "La dirección no puede estar vacía" }
        require(city.isNotBlank()) { "La ciudad no puede estar vacía" }
        require(country.isNotBlank()) { "El país no puede estar vacío" }
        require(price >= 0) { "El precio no puede ser negativo" }
        require(stars in 1..5) { "Las estrellas deben estar entre 1 y 5" }
        require(averageCustomerRating in 1..10) { "La calificación debe estar entre 1 y 10" }
        require(availableFrom.isBefore(availableTo)) {
            "La fecha de disponibilidad inicial debe ser anterior a la final"
        }
        require(maxOccupancy > 0) { "La ocupación máxima debe ser mayor a 0" }
        require(checkInTime.matches(Regex("^([01]?[0-9]|2[0-3]):[0-5][0-9]$"))) {
            "El formato de hora de check-in debe ser HH:mm"
        }
        require(checkOutTime.matches(Regex("^([01]?[0-9]|2[0-3]):[0-5][0-9]$"))) {
            "El formato de hora de check-out debe ser HH:mm"
        }
        roomSizeSquareMeters?.let {
            require(it > 0) { "El tamaño de la habitación debe ser mayor a 0" }
        }
    }

    /**
     * Verifica si el alojamiento está disponible en una fecha específica
     */
    fun isAvailableOn(date: LocalDateTime): Boolean =
        date.isAfter(availableFrom) && date.isBefore(availableTo)

    /**
     * Calcula la duración total de disponibilidad en días
     */
    fun getAvailabilityDuration(): Long =
        ChronoUnit.DAYS.between(availableFrom, availableTo)

    /**
     * Obtiene la foto principal del alojamiento
     */
    fun getMainPhoto(): String = photos.firstOrNull()?.url ?: ""

    /**
     * Verifica si el alojamiento es de alta categoría
     */
    fun isLuxury(): Boolean = stars >= 4 && averageCustomerRating >= 8

    /**
     * Obtiene la lista de amenidades como List<String>
     */
    fun getAmenitiesList(): List<String> =
        amenities.split(",").map { it.trim() }.filter { it.isNotEmpty() }

    fun toLodgingDto(): LodgingResponse.LodgingItem? {
        return if (id > 0) {
            LodgingResponse.LodgingItem(
                id = id,
                name = name,
                address = address,
                city = city,
                country = country,
                price = price,
                stars = stars,
                averageCustomerRating = averageCustomerRating,
                description = description,
                category = category.toDisplayString(),
                availableFrom = availableFrom,
                availableTo = availableTo,
                isFavorite = isFavorite,
                maxOccupancy = maxOccupancy,
                checkInTime = checkInTime,
                checkOutTime = checkOutTime,
                cancellationPolicy = cancellationPolicy,
                amenities = getAmenitiesList(),
                roomSizeSquareMeters = roomSizeSquareMeters,
                isPetFriendly = isPetFriendly,
                hasParking = hasParking,
                mainPhoto = getMainPhoto(),
                photos = photos.map { it.toPhotoDto() },



            )
        } else null

    }

}