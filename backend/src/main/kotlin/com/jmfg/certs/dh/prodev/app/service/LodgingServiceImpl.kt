package com.jmfg.certs.dh.prodev.app.service

import com.jmfg.certs.dh.prodev.model.Lodging
import com.jmfg.certs.dh.prodev.model.LodgingType
import com.jmfg.certs.dh.prodev.service.LodgingService
import com.jmfg.certs.dh.prodev.app.repository.LodgingRepository
import org.springframework.stereotype.Service

@Service
class LodgingServiceImpl(private val lodgingRepository: LodgingRepository) : LodgingService {

    override fun findAllLodgingTypes(): List<LodgingType> = lodgingRepository.findAllLodgingTypes()

    override fun findByType(type: LodgingType): List<Lodging> = lodgingRepository.findByType(type)
}