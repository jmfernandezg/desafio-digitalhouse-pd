package com.jmfg.certs.dh.prodev.model.dto

data class ReservationCreationRequest(
    val customerId: Long,
    val lodgingId: Long,
    val startDate: String,
    val endDate: String
)