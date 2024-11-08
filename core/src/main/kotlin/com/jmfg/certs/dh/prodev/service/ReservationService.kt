package com.jmfg.certs.dh.prodev.service

import com.jmfg.certs.dh.prodev.model.Reservation
import com.jmfg.certs.dh.prodev.model.dto.ReservationCreationRequest

interface ReservationService {
    fun findAll(): List<Reservation>
    fun delete(id: String)
    fun update(reservation: Reservation): Reservation
    fun findById(id: String): Reservation?
    fun create(request: ReservationCreationRequest) : Reservation
}