package com.jmfg.certs.dh.prodev.service

import com.jmfg.certs.dh.prodev.model.Customer
import com.jmfg.certs.dh.prodev.model.dto.CustomerCreationRequest
import com.jmfg.certs.dh.prodev.model.dto.CustomerResponse
import com.jmfg.certs.dh.prodev.model.dto.LoginRequest

interface CustomerService {
    fun login(request: LoginRequest): CustomerResponse.CustomerItem?

    fun findAll(): CustomerResponse

    fun create(request: CustomerCreationRequest): CustomerResponse.CustomerItem?

    fun delete(id: String)

    fun update(customer: Customer): CustomerResponse.CustomerItem
}