package com.jmfg.certs.dh.prodev.model.dto

data class LoginRequest(
    val username: String,
    val password: String
)

data class ReservationCreationRequest(
    val customerId: Long,
    val lodgingId: Long,
    val startDate: String,
    val endDate: String
)