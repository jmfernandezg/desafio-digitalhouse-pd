package com.jmfg.certs.dh.prodev.app

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@OpenAPIDefinition(
    info = Info(
        title = "DH Professional Developer Demo API",
        version = "1.0",
        description = "Backend API Documentation",
        contact = Contact(
            name = "Jose Fernandez", email = "jm.fernandez"
        )
    )
)
class BackendApplication

fun main(args: Array<String>) {
    runApplication<BackendApplication>(*args)
}