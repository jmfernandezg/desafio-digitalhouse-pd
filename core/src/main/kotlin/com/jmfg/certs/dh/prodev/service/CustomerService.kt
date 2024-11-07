package com.jmfg.certs.dh.prodev.service

interface CustomerService {
    fun login(username: String, password: String): String
}