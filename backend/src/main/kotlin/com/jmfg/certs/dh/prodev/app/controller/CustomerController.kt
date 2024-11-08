package com.jmfg.certs.dh.prodev.app.controller

import com.jmfg.certs.dh.prodev.model.Customer
import com.jmfg.certs.dh.prodev.model.dto.CustomerCreationRequest
import com.jmfg.certs.dh.prodev.model.dto.LoginRequest
import com.jmfg.certs.dh.prodev.service.CustomerService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/customer")
@CrossOrigin(origins = ["http://localhost:3000"])
class CustomerController(
    private val customerService: CustomerService
) {
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest) =
        customerService.login(request)

    @PostMapping
    fun create(@RequestBody request: CustomerCreationRequest) =
        customerService.create(request)

    @DeleteMapping
    fun delete(@RequestParam id: String) =
        customerService.delete(id)

    @PutMapping
    fun update(@RequestBody customer: Customer) =
        customerService.update(customer)

    @GetMapping
    fun findAll() =
        customerService.findAll()

}