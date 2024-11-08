package com.jmfg.certs.dh.prodev.service

import com.jmfg.certs.dh.prodev.model.Customer
import com.jmfg.certs.dh.prodev.model.dto.CustomerCreationRequest
import com.jmfg.certs.dh.prodev.model.dto.LoginRequest

interface CustomerService {
    fun login(request: LoginRequest): String?

    fun findAll(): List<Customer>

    fun create(request: CustomerCreationRequest): Customer?

    fun delete(id: String)

    fun update(customer: Customer): Customer
}