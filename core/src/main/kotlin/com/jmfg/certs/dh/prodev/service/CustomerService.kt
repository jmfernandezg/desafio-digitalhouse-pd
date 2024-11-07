package com.jmfg.certs.dh.prodev.service

import com.jmfg.certs.dh.prodev.model.dto.LoginRequest

interface CustomerService {
    fun login(request: LoginRequest): String
}