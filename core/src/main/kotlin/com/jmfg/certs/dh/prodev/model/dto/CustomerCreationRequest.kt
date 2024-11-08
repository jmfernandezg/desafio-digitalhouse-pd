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

data class LoginResponse(
    val authToken: String,
    val type: String = "Bearer",
    val expiresIn: Long = 3600
)