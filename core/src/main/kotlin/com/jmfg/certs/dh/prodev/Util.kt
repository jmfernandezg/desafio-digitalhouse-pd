package com.jmfg.certs.dh.prodev

import com.jmfg.certs.dh.prodev.model.Category
import java.text.Normalizer
import kotlin.math.pow
import kotlin.math.round
import kotlin.random.Random

/**
 * Clase de utilidad que proporciona funciones helper para operaciones comunes
 * en el sistema de gestión de alojamientos.
 */
object Util {

    /**
     * Convierte una cadena en formato snake_case a Capitalized Words.
     *
     * @param snakeString Cadena en formato snake_case
     * @return Cadena convertida a formato Capitalized Words
     * @throws IllegalArgumentException si la cadena de entrada es vacía o en blanco
     *
     * Ejemplo:
     * "hello_world" -> "Hello World"
     * "USER_NAME" -> "User Name"
     */
    fun toCapitalizedString(snakeString: String): String {
        require(snakeString.isNotBlank()) { "La cadena no puede estar vacía" }

        return snakeString
            .lowercase()
            .split('_')
            .joinToString(" ") { word ->
                word.replaceFirstChar { char -> char.uppercase() }
            }
    }

    /**
     * Convierte una cadena de texto a una categoría válida del sistema.
     *
     * @param name Nombre de la categoría
     * @return Objeto Category correspondiente
     * @throws IllegalArgumentException si el nombre no corresponde a una categoría válida
     */
    fun toCategory(name: String): Category {
        require(name.isNotBlank()) { "El nombre de la categoría no puede estar vacío" }

        val normalizedName = name
            .trim()
            .uppercase()
            .replace(" ", "_")
            .let { normalizeString(it) }

        return try {
            Category.valueOf(normalizedName)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Categoría inválida: $name")
        }
    }

    /**
     * Determina el grado de calidad basado en la calificación promedio del cliente.
     *
     * @param averageCustomerRating Calificación promedio (0-10)
     * @return Descripción textual del grado de calidad
     * @throws IllegalArgumentException si la calificación está fuera del rango válido
     */
    fun getGrade(averageCustomerRating: Int): Rating {
        require(averageCustomerRating in 0..10) {
            "La calificación debe estar entre 0 y 10"
        }

        return when (averageCustomerRating) {
            in 0..3 -> Rating.PESIMO
            in 4..5 -> Rating.MALO
            in 6..7 -> Rating.REGULAR
            in 7..8 -> Rating.BUENO
            in 8..9 -> Rating.MUY_BUENO
            else -> Rating.EXCELENTE
        }
    }

    /**
     * Calcula la distancia estimada desde el centro de la ciudad.
     *
     * @param address Dirección del alojamiento
     * @param precision Número de decimales para redondear (por defecto 1)
     * @return Distancia en kilómetros
     */
    fun getDistanceFromDowntown(
        address: String,
        precision: Int = 1
    ): Double {
        require(address.isNotBlank()) { "La dirección no puede estar vacía" }
        require(precision in 0..3) { "La precisión debe estar entre 0 y 3" }

        val distance = Random.nextDouble(0.0, 10.0)
        val factor = 10.0.pow(precision)
        return round(distance * factor) / factor
    }

    /**
     * Normaliza una cadena removiendo acentos y caracteres especiales.
     *
     * @param input Cadena a normalizar
     * @return Cadena normalizada
     */
    private fun normalizeString(input: String): String {
        return Normalizer
            .normalize(input, Normalizer.Form.NFD)
            .replace("[^\\p{ASCII}]".toRegex(), "")
    }

}

/**
 * Enumeración que representa los posibles grados de calificación
 * en el sistema.
 */
enum class Rating(val description: String, val range: IntRange) {
    PESIMO("Pésimo", 0..3),
    MALO("Malo", 4..5),
    REGULAR("Regular", 6..7),
    BUENO("Bueno", 7..8),
    MUY_BUENO("Muy bueno", 8..9),
    EXCELENTE("Excelente", 9..10);

    companion object {
        /**
         * Obtiene el Rating correspondiente a una calificación numérica.
         *
         * @param score Calificación numérica
         * @return Rating correspondiente
         */
        fun fromScore(score: Int): Rating {
            require(score in 0..10) { "La calificación debe estar entre 0 y 10" }
            return values().first { score in it.range }
        }
    }
}