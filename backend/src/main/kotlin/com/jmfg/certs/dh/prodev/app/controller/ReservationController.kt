package com.jmfg.certs.dh.prodev.app.controller

import com.jmfg.certs.dh.prodev.model.Reservation
import com.jmfg.certs.dh.prodev.model.dto.ReservationCreationRequest
import com.jmfg.certs.dh.prodev.service.ReservationService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/reservation")
@CrossOrigin(origins = ["http://localhost:3000"])
class ReservationController(private val reservationService: ReservationService) {

    @PostMapping("/")
    fun create(@RequestBody createRequest: ReservationCreationRequest) = reservationService.create(createRequest)

    @GetMapping("/")
    fun findAll(): List<Reservation> = reservationService.findAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String) = reservationService.findById(id)

    @PutMapping("/")
    fun update(@RequestBody reservation: Reservation) = reservationService.update(reservation)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) = reservationService.delete(id)


}
