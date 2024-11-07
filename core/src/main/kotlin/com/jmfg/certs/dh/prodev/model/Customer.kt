package com.jmfg.certs.dh.prodev.model

import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

@Entity
data class Customer(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: String = UUID.randomUUID().toString(),
    val username: String = "",
    val password: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val dob: LocalDate = LocalDate.now(),
    val email: String = "",
    @OneToMany(mappedBy = "customer")
    val reservations: List<Reservation> = mutableListOf()
) : BaseEntity()

