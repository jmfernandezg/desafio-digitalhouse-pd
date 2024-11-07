package com.jmfg.certs.dh.prodev.app.controller

import com.jmfg.certs.dh.prodev.model.Category
import com.jmfg.certs.dh.prodev.model.Lodging
import com.jmfg.certs.dh.prodev.service.LodgingService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/lodging")
class LodgingController(private val lodgingService: LodgingService) {

    @GetMapping("/categories")
    fun getAllCategories(): List<Category> = lodgingService.findAllCategories()

    @GetMapping("/lodgings/{category}")
    fun getLodgingsByCategory(@PathVariable category: String): List<Lodging> =
        lodgingService.findByCategory(Category.valueOf(category.uppercase()))
}
