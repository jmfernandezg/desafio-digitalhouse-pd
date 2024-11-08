package com.jmfg.certs.dh.prodev.model.dto

import com.jmfg.certs.dh.prodev.model.Category
import java.time.LocalDateTime

data class LodgingCreationRequest(
    val name: String,
    val address: String,
    val rating: Double,
    val price: Double,
    val description: String,
    val category: Category,
    val availableFrom: LocalDateTime,
    val availableTo: LocalDateTime
)