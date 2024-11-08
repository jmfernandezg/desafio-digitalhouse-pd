package com.jmfg.certs.dh.prodev.model.dto

data class CustomerCreationRequest(
    val username: String,
    val password: String,
    val email: String,
    val firstName: String,
    val lastName: String
)

data class LoginRequest(
    val username: String,
    val password: String
)

