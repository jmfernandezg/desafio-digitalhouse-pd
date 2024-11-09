package com.jmfg.certs.dh.prodev.app.controller

import com.jmfg.certs.dh.prodev.model.Category
import com.jmfg.certs.dh.prodev.model.Lodging
import com.jmfg.certs.dh.prodev.model.dto.CategoryResponse
import com.jmfg.certs.dh.prodev.model.dto.LodgingCreationRequest
import com.jmfg.certs.dh.prodev.service.LodgingService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/lodging")
@CrossOrigin(origins = ["http://localhost:3000"])
class LodgingController(private val lodgingService: LodgingService) {

    @GetMapping("/categories")
    fun getAllCategories(): CategoryResponse = lodgingService.findAllCategories()

    @GetMapping("/{category}")
    fun getLodgingsByCategory(@PathVariable category: String): List<Lodging> =
        lodgingService.findByCategory(Category.valueOf(category.uppercase()))

    @GetMapping
    fun getAllLodgings(): List<Lodging> = lodgingService.findAll()

    @PostMapping
    fun createLodging(@RequestBody request: LodgingCreationRequest): Lodging = lodgingService.create(request)

    @DeleteMapping("/{id}")
    fun deleteLodging(@PathVariable id: String) = lodgingService.delete(id)
}
