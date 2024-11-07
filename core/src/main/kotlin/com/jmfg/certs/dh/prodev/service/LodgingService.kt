package com.jmfg.certs.dh.prodev.service

import com.jmfg.certs.dh.prodev.model.Category
import com.jmfg.certs.dh.prodev.model.Lodging

interface LodgingService {
    fun findAllCategories(): List<Category>
    fun findByCategory(category: Category): List<Lodging>
}