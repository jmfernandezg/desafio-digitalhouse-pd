package com.jmfg.certs.dh.prodev.service

import com.jmfg.certs.dh.prodev.model.Category
import com.jmfg.certs.dh.prodev.model.Lodging
import com.jmfg.certs.dh.prodev.model.dto.CategoryResponse
import com.jmfg.certs.dh.prodev.model.dto.LodgingCreationRequest
import com.jmfg.certs.dh.prodev.model.dto.LodgingResponse
import com.jmfg.certs.dh.prodev.model.dto.LodgingResponse.LodgingItem

interface LodgingService {
    fun findById(id: String): LodgingItem?
    fun findAll(): LodgingResponse
    fun create(request: LodgingCreationRequest): LodgingItem
    fun delete(id: String)
    fun update(lodging: Lodging): LodgingItem
    fun findAllCategories(): CategoryResponse
    fun findByCategory(category: Category): LodgingResponse
    fun findAllCities(): Set<String>
}