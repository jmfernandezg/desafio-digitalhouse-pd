package com.jmfg.certs.dh.prodev.app.repository


import com.jmfg.certs.dh.prodev.model.Lodging
import com.jmfg.certs.dh.prodev.model.Photo
import com.jmfg.certs.dh.prodev.model.Reservation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface PhotoRepository : JpaRepository<Photo, String>

@Repository
interface ReservationRepository : JpaRepository<Reservation, String>

@Repository
interface LodgingRepository : JpaRepository<Lodging, String>