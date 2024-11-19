package com.jmfg.certs.dh.prodev.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.jmfg.certs.dh.prodev.Util
import com.jmfg.certs.dh.prodev.model.dto.LodgingResponse
import com.jmfg.certs.dh.prodev.model.dto.LodgingSearchRequest
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
data class Lodging(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val address: String = "",
    val city: String = "",
    val country: String = "",
    val price: Double = 0.0,
    val stars: Int = 3,
    val averageCustomerRating: Int = 7,
    val description: String = "",
    @Enumerated(EnumType.STRING)
    val category: Category = Category.HOTEL,
    val availableFrom: LocalDateTime = LocalDateTime.now(),
    val availableTo: LocalDateTime = LocalDateTime.now(),
    val isFavorite: Boolean = false,
    @OneToMany(mappedBy = "lodging", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val photos: List<Photo> = mutableListOf()
) : BaseEntity()


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
    category = category.toCapitalizedString(),
    availableFrom = this.availableFrom,
    availableTo = this.availableTo,
    isFavorite = this.isFavorite,
    photos = this.photos.map { it.url },
    displayPhoto = this.photos.firstOrNull()?.url ?: "",
    grade = Util.getGrade(this.averageCustomerRating),
    distanceFromDownTown = Util.getDistanceFromDownTown(this.address)
)


fun Lodging.matchesSearchCriteria(request: LodgingSearchRequest): Boolean {

    val countryMatch = this.country.contains(request.destination, ignoreCase = true)
    val cityMatch = this.city.contains(request.destination, ignoreCase = true)
    val dateMatch =
        this.availableFrom.toLocalDate() <= request.checkIn && this.availableTo.toLocalDate() >= request.checkOut

    println("Lodging ID: ${this.id} Match: (City ($cityMatch): ${this.city}, Country ($countryMatch): ${this.country}, Request Destination: ${request.destination})")
    println("Date Match: $dateMatch (Available From: ${this.availableFrom}, Available To: ${this.availableTo}, Check-In: ${request.checkIn}, Check-Out: ${request.checkOut})")

    return countryMatch && cityMatch && dateMatch
}

enum class Category {
    HOTEL, HOSTEL, DEPARTMENT, BED_AND_BREAKFAST
}

fun Category.toCapitalizedString(): String = Util.toCapitalizedString(this.name)

@Entity
data class Photo(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = UUID.randomUUID().toString(),
    val url: String = "",
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    val lodging: Lodging? = null
) : BaseEntity()