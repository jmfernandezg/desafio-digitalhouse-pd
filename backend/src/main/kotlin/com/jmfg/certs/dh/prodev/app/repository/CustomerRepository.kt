package com.jmfg.certs.dh.prodev.app.repository

import com.jmfg.certs.dh.prodev.model.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository : JpaRepository<Customer, String> {
    fun findByUsernameAndPassword(username: String, password: String): Customer?
}