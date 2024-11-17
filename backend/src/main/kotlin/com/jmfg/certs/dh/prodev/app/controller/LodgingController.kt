package com.jmfg.certs.dh.prodev.app.controller

import com.jmfg.certs.dh.prodev.Util
import com.jmfg.certs.dh.prodev.model.dto.CategoryResponse
import com.jmfg.certs.dh.prodev.model.dto.LodgingCreationRequest
import com.jmfg.certs.dh.prodev.model.dto.LodgingResponse
import com.jmfg.certs.dh.prodev.model.dto.LodgingResponse.LodgingItem
import com.jmfg.certs.dh.prodev.service.LodgingService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/v1/lodging")
@CrossOrigin(origins = ["http://localhost:3000"])
class LodgingController(private val lodgingService: LodgingService) {

    @GetMapping("/categories")
    fun getAllCategories(): CategoryResponse = lodgingService.findAllCategories()

    @GetMapping("/categories/{category}")
    fun getLodgingsByCategory(@PathVariable category: String): LodgingResponse =
        lodgingService.findByCategory(Util.toCategory(category))

    @GetMapping("/{id}")
    fun getLodgingById(@PathVariable id: String): LodgingItem? =
        lodgingService.findById(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Lodging not found")

    @GetMapping
    fun getAllLodgings(): LodgingResponse = lodgingService.findAll()

    @PostMapping
    fun createLodging(@RequestBody request: LodgingCreationRequest): LodgingItem = lodgingService.create(request)

    @DeleteMapping("/{id}")
    fun deleteLodging(@PathVariable id: String) = lodgingService.delete(id)
}
