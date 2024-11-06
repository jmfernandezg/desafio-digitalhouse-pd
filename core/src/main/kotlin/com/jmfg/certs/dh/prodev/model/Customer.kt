package com.jmfg.certs.dh.prodev.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import java.util.*

@Entity
data class Customer(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: String = UUID.randomUUID().toString(),
    val username: String = "",
    val password: String = "",
    val email: String = "",
    @OneToMany(mappedBy = "customer")
    val reservations: List<Reservation> = mutableListOf()
) : BaseEntity()

