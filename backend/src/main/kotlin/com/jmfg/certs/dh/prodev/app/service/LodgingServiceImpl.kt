package com.jmfg.certs.dh.prodev.app.service

import com.jmfg.certs.dh.prodev.model.Lodging
import com.jmfg.certs.dh.prodev.model.Category
import com.jmfg.certs.dh.prodev.service.LodgingService
import com.jmfg.certs.dh.prodev.app.repository.LodgingRepository
import org.springframework.stereotype.Service

@Service
class LodgingServiceImpl(private val lodgingRepository: LodgingRepository) : LodgingService {

    override fun findAllCategories(): List<Category> = lodgingRepository.findAllCategories()

    override fun findByCategory(category: Category): List<Lodging> = lodgingRepository.findByCategory(category)
}