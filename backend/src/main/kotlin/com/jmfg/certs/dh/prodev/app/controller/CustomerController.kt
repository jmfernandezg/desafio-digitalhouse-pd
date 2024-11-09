package com.jmfg.certs.dh.prodev.app.controller

import com.jmfg.certs.dh.prodev.model.Customer
import com.jmfg.certs.dh.prodev.model.dto.CustomerCreationRequest
import com.jmfg.certs.dh.prodev.model.dto.LoginRequest
import com.jmfg.certs.dh.prodev.model.dto.LoginResponse
import com.jmfg.certs.dh.prodev.service.CustomerService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/v1/customer")
@CrossOrigin(origins = ["http://localhost:3000"])
@Tag(name = "Customer", description = "Customer management APIs")
class CustomerController(
    private val customerService: CustomerService
) {
    @PostMapping("/login")
    @Operation(summary = "Login a customer", description = "Authenticate a customer and return a JWT token")
    fun login(@RequestBody request: LoginRequest): LoginResponse =
        customerService.login(request)?.let {
            LoginResponse(it)
        } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found")

    @PostMapping
    @Operation(summary = "Create a new customer", description = "Create a new customer with the provided details")
    fun create(@RequestBody request: CustomerCreationRequest) =
        customerService.create(request) ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "Customer already exists"
        )

    @DeleteMapping
    @Operation(summary = "Delete a customer", description = "Delete a customer by ID")
    fun delete(@RequestParam id: String) =
        customerService.delete(id)

    @PutMapping
    @Operation(summary = "Update a customer", description = "Update the details of an existing customer")
    fun update(@RequestBody customer: Customer) =
        customerService.update(customer)

    @GetMapping
    @Operation(summary = "Get all customers", description = "Retrieve a list of all customers")
    fun findAll() =
        customerService.findAll()

}