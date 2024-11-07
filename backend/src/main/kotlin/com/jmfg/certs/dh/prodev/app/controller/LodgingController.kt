package com.jmfg.certs.dh.prodev.app.controller

import com.jmfg.certs.dh.prodev.model.Lodging
import com.jmfg.certs.dh.prodev.model.LodgingType
import com.jmfg.certs.dh.prodev.service.LodgingService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/lodging")
class LodgingController(private val lodgingService: LodgingService) {

    @GetMapping("/types")
    fun getAllLodgingTypes(): List<LodgingType> = lodgingService.findAllLodgingTypes()

    @GetMapping("/lodgings/{type}")
    fun getLodgingsByType(@PathVariable type: String): List<Lodging> =
        lodgingService.findByType(LodgingType.valueOf(type.uppercase()))
}
