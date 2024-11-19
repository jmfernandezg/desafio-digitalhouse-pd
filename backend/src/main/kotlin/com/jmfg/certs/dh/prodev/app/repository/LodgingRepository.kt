package com.jmfg.certs.dh.prodev.app.repository

import com.jmfg.certs.dh.prodev.model.Category
import com.jmfg.certs.dh.prodev.model.Lodging
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

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

    @Query("SELECT l FROM Lodging l WHERE :location LIKE CONCAT('%', l.city, '%') AND :location LIKE CONCAT('%', l.country, '%') AND l.availableFrom <= :checkIn AND l.availableTo >= :checkOut")
    fun findLodgingsByLocationAndDates(
        @Param("location") location: String,
        @Param("checkIn") checkIn: LocalDateTime,
        @Param("checkOut") checkOut: LocalDateTime
    ): List<Lodging>


}