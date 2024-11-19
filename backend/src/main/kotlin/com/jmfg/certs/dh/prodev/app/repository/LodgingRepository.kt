package com.jmfg.certs.dh.prodev.app.repository

import com.jmfg.certs.dh.prodev.model.Category
import com.jmfg.certs.dh.prodev.model.Lodging
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface LodgingRepository : JpaRepository<Lodging, String> {
    @EntityGraph(attributePaths = ["photos"])
    fun findByCategory(category: Category): List<Lodging>

    @EntityGraph(attributePaths = ["photos"])
    override fun findAll(): List<Lodging>

    @Query("SELECT DISTINCT l.category FROM Lodging l")
    fun findAllCategories(): List<Category>

    @Query("SELECT DISTINCT CONCAT(l.city, ', ', l.country) FROM Lodging l")
    fun findAllCities(): Set<String>

}