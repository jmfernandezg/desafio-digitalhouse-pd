package com.jmfg.certs.dh.prodev.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
data class Lodging(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val address: String = "",
    val rating: Double = 0.0,
    val price: Double = 0.0,
    val description: String = "",
    @Enumerated(EnumType.STRING)
    val category: Category = Category.HOTEL,
    val availableFrom: LocalDateTime = LocalDateTime.now(),
    val availableTo: LocalDateTime = LocalDateTime.now(),
    @OneToMany(mappedBy = "lodging", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val photos: List<Photo> = mutableListOf()
) : BaseEntity()

enum class Category {
    HOTEL, HOSTEL, DEPARTMENT, BED_AND_BREAKFAST
}

@Entity
data class Photo(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: String = UUID.randomUUID().toString(),
    val url: String = "",
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lodging_id")
    @JsonIgnore
    val lodging: Lodging? = null
) : BaseEntity()