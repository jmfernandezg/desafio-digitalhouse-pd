package com.jmfg.certs.dh.prodev.app.repository


import com.jmfg.certs.dh.prodev.model.Reservation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface ReservationRepository : JpaRepository<Reservation, String>

