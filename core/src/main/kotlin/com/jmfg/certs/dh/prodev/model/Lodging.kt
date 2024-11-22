package com.jmfg.certs.dh.prodev.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.jmfg.certs.dh.prodev.Util
import com.jmfg.certs.dh.prodev.model.dto.LodgingResponse
import jakarta.persistence.*
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

/**
 * Categorías disponibles de alojamiento
 */
enum class Category {
    HOTEL,
    HOSTEL,
    DEPARTMENT,
    BED_AND_BREAKFAST;

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
 */
@Entity
@Table(
    name = "lodgings",
    indexes = [
        Index(name = "idx_lodging_city", columnList = "city"),
        Index(name = "idx_lodging_category", columnList = "category"),
        Index(name = "idx_lodging_price", columnList = "price")
    ]
)
data class Lodging(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = UUID.randomUUID().toString(),

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

    companion object {
        /**
         * Crea una instancia de prueba
         */
        fun createTestInstance() = Lodging(
            name = "Test Hotel",
            address = "Test Address 123",
            city = "Test City",
            country = "Test Country",
            price = 100.0,
            stars = 4,
            category = Category.HOTEL,
            availableFrom = LocalDateTime.now(),
            availableTo = LocalDateTime.now().plusMonths(1)
        )
    }
}

/**
 * Entidad que representa una foto de un alojamiento
 */
@Entity
@Table(name = "photos")
data class Photo(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false)
    val url: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lodging_id", nullable = false)
    @JsonIgnore
    val lodging: Lodging? = null
) : BaseEntity() {

    init {
        require(url.isNotBlank()) { "La URL de la foto no puede estar vacía" }
        require(url.startsWith("http")) { "La URL debe ser válida" }
    }
}

/**
 * Convierte un Lodging a su representación DTO
 */
fun Lodging.toLodgingDto(): LodgingResponse.LodgingItem = LodgingResponse.LodgingItem(
    id = this.id,
    name = this.name,
    address = this.address,
    city = this.city,
    country = this.country,
    price = this.price,
    stars = this.stars,
    averageCustomerRating = this.averageCustomerRating,
    description = this.description,
    category = category.toDisplayString(),
    availableFrom = this.availableFrom,
    availableTo = this.availableTo,
    isFavorite = this.isFavorite,
    photos = this.photos.map { it.url },
    displayPhoto = this.getMainPhoto(),
    grade = Util.getGrade(this.averageCustomerRating),
    distanceFromDownTown = Util.getDistanceFromDownTown(this.address)
)