package com.jmfg.certs.dh.prodev.app.service

import com.jmfg.certs.dh.prodev.app.repository.LodgingRepository
import com.jmfg.certs.dh.prodev.model.Category
import com.jmfg.certs.dh.prodev.model.Lodging
import com.jmfg.certs.dh.prodev.model.dto.CategoryResponse
import com.jmfg.certs.dh.prodev.model.dto.LodgingCreationRequest
import com.jmfg.certs.dh.prodev.service.LodgingService
import org.springframework.stereotype.Service

@Service
class LodgingServiceImpl(private val lodgingRepository: LodgingRepository) : LodgingService {

    override fun create(request: LodgingCreationRequest): Lodging = Lodging(
        name = request.name,
        description = request.description,
        category = request.category,
        price = request.price,
        address = request.address,
        rating = request.rating,
        availableFrom = request.availableFrom,
        availableTo = request.availableTo
    ).run {
        lodgingRepository.save(this)
    }

    override fun update(lodging: Lodging): Lodging = lodgingRepository.save(lodging)

    override fun findAll(): List<Lodging> = lodgingRepository.findAll()

    override fun findAllCategories(): CategoryResponse = lodgingRepository.findAllCategories().map { category ->
        CategoryResponse.Category(
            category.name,
            "/images/${category.name.lowercase()}.jpg",
            lodgingRepository.findByCategory(category).size
        )
    }.let { CategoryResponse(it) }

    override fun findByCategory(category: Category): List<Lodging> = lodgingRepository.findByCategory(category)

    override fun delete(id: String) = lodgingRepository.deleteById(id)
}