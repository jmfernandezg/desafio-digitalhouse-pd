package com.jmfg.certs.dh.prodev.model.dto

/**
 * Respuesta que contiene información sobre las categorías de alojamientos
 *
 * Esta clase representa la estructura de datos para transmitir información
 * sobre las diferentes categorías de alojamientos disponibles en el sistema.
 *
 * @property categories Lista de categorías con sus detalles
 */
data class CategoryResponse(
    val categories: List<CategoryItem>
) {
    /**
     * Representa una categoría individual de alojamiento
     *
     * @property name Nombre identificador de la categoría (ej: "HOTEL", "HOSTEL")
     * @property imageUrl URL de la imagen representativa de la categoría
     * @property count Cantidad total de alojamientos en esta categoría
     */
    data class CategoryItem(
        val name: String,
        val imageUrl: String,
        val displayName: String,
        val count: Int
    )
}