package com.jmfg.certs.dh.prodev.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.jmfg.certs.dh.prodev.model.dto.CustomerResponse
import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

@Entity
data class Customer(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = UUID.randomUUID().toString(),
    val username: String = "",
    @JsonIgnore
    val password: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val dob: LocalDate = LocalDate.now(),
    val email: String = "",
    @OneToMany(mappedBy = "customer")
    val reservations: List<Reservation> = mutableListOf()
) : BaseEntity()

fun Customer.toCustomerItem() = CustomerResponse.CustomerItem(
    id = this.id,
    username = this.username,
    email = this.email,
    firstName = this.firstName,
    lastName = this.lastName
)