package com.jmfg.certs.dh.prodev.app.service

import com.jmfg.certs.dh.prodev.app.repository.LodgingRepository
import com.jmfg.certs.dh.prodev.model.Category
import com.jmfg.certs.dh.prodev.model.Lodging
import com.jmfg.certs.dh.prodev.model.dto.CategoryResponse
import com.jmfg.certs.dh.prodev.model.dto.LodgingCreationRequest
import com.jmfg.certs.dh.prodev.model.dto.LodgingResponse
import com.jmfg.certs.dh.prodev.model.dto.LodgingResponse.LodgingItem
import com.jmfg.certs.dh.prodev.model.toCapitalizedString
import com.jmfg.certs.dh.prodev.model.toLodgingDto
import com.jmfg.certs.dh.prodev.service.LodgingService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class LodgingServiceImpl(private val lodgingRepository: LodgingRepository) : LodgingService {

    override fun create(request: LodgingCreationRequest): LodgingItem = Lodging(
        name = request.name,
        description = request.description,
        category = request.category,
        price = request.price,
        address = request.address,
        stars = request.stars,
        averageCustomerRating = request.averageCustomerRating,
        availableFrom = request.availableFrom,
        availableTo = request.availableTo
    ).run {
        lodgingRepository.save(this)
    }.toLodgingDto()

    override fun update(lodging: Lodging): LodgingItem = lodging.run {
        lodgingRepository.save(this)
    }.toLodgingDto()

    override fun findById(id: String): LodgingItem? =
        lodgingRepository.findByIdOrNull(id)
            ?.toLodgingDto()

    override fun findAll(): LodgingResponse =
        lodgingRepository.findAll().map { it.toLodgingDto() }.let { LodgingResponse(it) }

    override fun findAllCategories(): CategoryResponse = lodgingRepository.findAllCategories().map { category ->
        val lodgings = lodgingRepository.findByCategory(category)
        CategoryResponse.CategoryItem(
            category.toCapitalizedString(),
            lodgings.flatMap { it.photos }.map { it.url }.first(),
            lodgings.size
        )
    }.let { CategoryResponse(it) }

    override fun findByCategory(category: Category): LodgingResponse =
        lodgingRepository.findByCategory(category).map {
            it.toLodgingDto()
        }.let { LodgingResponse(it) }

    override fun delete(id: String) = lodgingRepository.deleteById(id)
}