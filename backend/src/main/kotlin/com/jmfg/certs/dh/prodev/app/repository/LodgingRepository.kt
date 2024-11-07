package com.jmfg.certs.dh.prodev.app.repository

import com.jmfg.certs.dh.prodev.model.Lodging
import com.jmfg.certs.dh.prodev.model.LodgingType
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface LodgingRepository : JpaRepository<Lodging, String> {
    @EntityGraph(attributePaths = ["photos"])
    fun findByType(type: LodgingType): List<Lodging>

    @Query("SELECT DISTINCT l.type FROM Lodging l")
    fun findAllLodgingTypes(): List<LodgingType>

}