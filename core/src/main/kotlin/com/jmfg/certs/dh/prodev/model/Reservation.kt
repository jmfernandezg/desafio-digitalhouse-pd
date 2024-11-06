package com.jmfg.certs.dh.prodev.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import java.time.LocalDateTime
import jakarta.persistence.Column
import jakarta.persistence.OneToMany


@Entity
data class Reservation(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val reservationType: ReservationType,
    val startDate: LocalDateTime = LocalDateTime.now(),
    val endDate: LocalDateTime = LocalDateTime.now(),
    @ManyToOne
    val flight: Flight? = null,
    @ManyToOne
    val car: Car? = null,
    @ManyToOne
    val hotel: Hotel? = null, 
    @ManyToOne
    val customer: Customer = Customer()
): BaseEntity()

enum class ReservationType {
    FLIGHT, CAR, HOTEL
}

@Entity
data class Hotel(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    val address: String,
    val city: String,
    val country: String,
    val pricePerNight: Double
): BaseEntity()

@Entity
data class Car(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val make: String,
    val model: String,
    val year: Int,
    val pricePerDay: Double
): Product()

@Entity
data class Flight(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val airline: String,
    val flightNumber: String,
    val departureAirport: String,
    val arrivalAirport: String,
    val departureTime: String,
    val arrivalTime: String,
    val price: Double
): Product()

@Entity
data class Photos(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val url: String,
    val description: String? = null,
    val uploadedBy: String
) : BaseEntity()

abstract class Product {
    @OneToMany(mappedBy = "product")
    val photos: List<Photos> = mutableListOf()
}: BaseEntity()

