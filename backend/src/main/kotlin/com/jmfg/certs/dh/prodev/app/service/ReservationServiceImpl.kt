package com.jmfg.certs.dh.prodev.app.service

import com.jmfg.certs.dh.prodev.app.repository.CustomerRepository
import com.jmfg.certs.dh.prodev.app.repository.LodgingRepository
import com.jmfg.certs.dh.prodev.app.repository.ReservationRepository
import com.jmfg.certs.dh.prodev.model.Reservation
import com.jmfg.certs.dh.prodev.model.dto.ReservationCreationRequest
import com.jmfg.certs.dh.prodev.service.ReservationService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class ReservationServiceImpl(
    private val reservationRepository: ReservationRepository,
    private val customerRepository: CustomerRepository,
    private val lodgingRepository: LodgingRepository
) : ReservationService {
    override fun findAll(): List<Reservation> = reservationRepository.findAll()

    override fun delete(id: String) = reservationRepository.deleteById(id)

    override fun update(reservation: Reservation) = reservationRepository.save(reservation)

    override fun findById(id: String): Reservation? = reservationRepository.findByIdOrNull(id)


    override fun create(request: ReservationCreationRequest): Reservation {
        val customer = customerRepository.findByIdOrNull(request.customerId)
            ?: throw IllegalArgumentException("Customer not found")
        val lodging =
            lodgingRepository.findByIdOrNull(request.lodgingId) ?: throw IllegalArgumentException("Lodging not found")
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val startDate = LocalDateTime.parse(request.startDate, formatter)
        val endDate = LocalDateTime.parse(request.endDate, formatter)

        val reservation = Reservation(
            customer = customer, lodging = lodging, startDate = startDate, endDate = endDate
        )
        return reservationRepository.save(reservation)
    }
}