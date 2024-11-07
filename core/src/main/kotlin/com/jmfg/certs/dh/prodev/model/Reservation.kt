package com.jmfg.certs.dh.prodev.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
data class Reservation(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: String = UUID.randomUUID().toString(),
    val startDate: LocalDateTime = LocalDateTime.now(),
    val endDate: LocalDateTime = LocalDateTime.now(),
    @ManyToOne
    val customer: Customer = Customer(),
    @ManyToOne
    val lodging: Lodging = Lodging()
) : BaseEntity()