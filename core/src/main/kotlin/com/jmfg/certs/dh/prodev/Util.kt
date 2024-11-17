package com.jmfg.certs.dh.prodev

import kotlin.random.Random.Default.nextDouble

object Util {
    fun toCapitalizedString(snakeString: String): String {
        return snakeString.lowercase().split('_').joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
    }

    fun getGrade(averageCustomerRating: Int): String {
        return when (averageCustomerRating) {
            in 0..3 -> "Pésimo"
            in 3..6 -> "Malo"
            in 6..7 -> "Regular"
            in 7..8 -> "Bueno"
            in 8..9 -> "Muy bueno"
            in 9..10 -> "Excelente"
            else -> "Unknown"
        }
    }

    fun getDistanceFromDownTown(address: String) = nextDouble(0.0, 10.0)
}