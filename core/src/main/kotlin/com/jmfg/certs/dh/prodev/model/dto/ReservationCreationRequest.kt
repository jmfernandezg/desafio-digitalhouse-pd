package com.jmfg.certs.dh.prodev.model.dto

data class ReservationCreationRequest(
    val customerId: String, val lodgingId: String, val startDate: String, val endDate: String
)