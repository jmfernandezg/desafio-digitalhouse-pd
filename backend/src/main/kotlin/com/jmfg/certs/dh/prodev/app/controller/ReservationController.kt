package com.jmfg.certs.dh.prodev.app.controller

import com.jmfg.certs.dh.prodev.model.Reservation
import com.jmfg.certs.dh.prodev.model.dto.ReservationCreationRequest
import com.jmfg.certs.dh.prodev.service.ReservationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/reservation")
class ReservationController(private val reservationService: ReservationService) {

    @PostMapping("/")
    fun create(@RequestBody createRequest: ReservationCreationRequest): Reservation = TODO()
}
