package com.jmfg.certs.dh.prodev.service

import com.jmfg.certs.dh.prodev.model.Lodging
import com.jmfg.certs.dh.prodev.model.LodgingType

interface LodgingService {
    fun findAllLodgingTypes(): List<LodgingType>
    fun findByType(type: LodgingType): List<Lodging>
}