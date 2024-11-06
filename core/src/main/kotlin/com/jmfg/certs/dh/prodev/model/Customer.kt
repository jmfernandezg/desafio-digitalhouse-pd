package com.jmfg.certs.dh.prodev.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
data class Customer(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val usernamed: String = "",
    val password: String = "",
    val email: String = "",
    @OneToMany(mappedBy = "customer")
    val reservations: List<Reservation> = mutableListOf()
)
