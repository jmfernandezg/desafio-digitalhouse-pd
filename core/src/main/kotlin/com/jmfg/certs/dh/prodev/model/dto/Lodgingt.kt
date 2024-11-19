package com.jmfg.certs.dh.prodev.model.dto

import com.jmfg.certs.dh.prodev.model.Category
import java.time.LocalDate
import java.time.LocalDateTime

data class LodgingCreationRequest(
    val name: String,
    val address: String,
    val stars: Int,
    val averageCustomerRating : Int,
    val price: Double,
    val description: String,
    val category: Category,
    val availableFrom: LocalDateTime,
    val availableTo: LocalDateTime
)

data class LodgingSearchRequest(
    val destination: String,
    val checkIn: LocalDate,
    val checkOut: LocalDate
)

data class LodgingResponse(val lodgings: List<LodgingItem>) {
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
    )
}