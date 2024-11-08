package com.jmfg.certs.dh.prodev.service

import com.jmfg.certs.dh.prodev.model.Category
import com.jmfg.certs.dh.prodev.model.Lodging
import com.jmfg.certs.dh.prodev.model.dto.LodgingCreationRequest

interface LodgingService {
    fun findAll(): List<Lodging>
    fun create(request: LodgingCreationRequest): Lodging
    fun delete(id: String)
    fun update(lodging: Lodging): Lodging
    fun findAllCategories(): List<Category>
    fun findByCategory(category: Category): List<Lodging>
}