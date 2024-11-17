package com.jmfg.certs.dh.prodev

import kotlin.random.Random.Default.nextDouble

object Util {
    fun toCapitalizedString(snakeString: String): String {
        return snakeString.lowercase().split('_').joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
    }

    fun getGrade(averageCustomerRating: Int): String {
        return when (averageCustomerRating) {
            in 0..2 -> "Malo"
            in 3..4 -> "Regular"
            in 5..6 -> "Bueno"
            in 7..8 -> "Muy bueno"
            in 9..10 -> "Excelente"
            else -> "Unknown"
        }
    }

    fun getDistanceFromDownTown(address: String) = nextDouble(0.0, 10.0)
}