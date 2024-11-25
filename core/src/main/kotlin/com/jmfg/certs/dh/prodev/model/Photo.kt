package com.jmfg.certs.dh.prodev.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.LocalDateTime

/**
 * Entidad que representa una foto de un alojamiento
 *
 * Mejoras implementadas:
 * - ID numérico para mejor rendimiento
 * - Campos adicionales para mejor gestión de fotos
 * - Validaciones mejoradas
 * - Índices optimizados
 * - Tipos de fotos categorizados
 */
@Entity
@Table(
    name = "photos",
    indexes = [
        Index(name = "idx_photo_type", columnList = "photo_type"),
        Index(name = "idx_photo_lodging", columnList = "lodging_id"),
        Index(name = "idx_photo_main", columnList = "is_main")
    ]
)
data class Photo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val url: String = "",

    @Column(nullable = false, name = "file_name")
    val fileName: String = "",

    @Column(name = "photo_type")
    @Enumerated(EnumType.STRING)
    val photoType: PhotoType = PhotoType.ROOM,

    @Column(name = "upload_date")
    val uploadDate: LocalDateTime = LocalDateTime.now(),

    @Column(name = "is_main")
    val isMain: Boolean = false,

    @Column(name = "alt_text")
    val altText: String = "",

    @Column(name = "width_px")
    val widthPx: Int? = null,

    @Column(name = "height_px")
    val heightPx: Int? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lodging_id", nullable = false)
    @JsonIgnore
    val lodging: Lodging? = null
) : BaseEntity() {

    /**
     * Tipos de fotos disponibles para un alojamiento
     */
    enum class PhotoType {
        ROOM,
        EXTERIOR,
        BATHROOM,
        VIEW,
        AMENITY,
        RESTAURANT,
        POOL,
        GYM,
        COMMON_AREA;

        /**
         * Convierte el tipo de foto a un string para mostrar
         */
        fun toDisplayString(): String = name.lowercase().replace('_', ' ').capitalize()
    }

    init {
        require(url.isNotBlank()) { "La URL de la foto no puede estar vacía" }
        require(url.startsWith("http")) { "La URL debe ser válida y comenzar con http" }
        require(fileName.isNotBlank()) { "El nombre del archivo no puede estar vacío" }
        require(altText.isNotBlank()) { "El texto alternativo no puede estar vacío" }

        widthPx?.let {
            require(it > 0) { "El ancho debe ser mayor a 0" }
        }
        heightPx?.let {
            require(it > 0) { "El alto debe ser mayor a 0" }
        }
    }

    /**
     * Verifica si la imagen es de alta resolución
     */
    fun isHighResolution(): Boolean =
        (widthPx ?: 0) >= 1920 && (heightPx ?: 0) >= 1080

    /**
     * Obtiene la relación de aspecto de la imagen
     */
    fun getAspectRatio(): Double? =
        if (widthPx != null && heightPx != null && heightPx != 0) {
            widthPx.toDouble() / heightPx.toDouble()
        } else null

    /**
     * Genera una URL para una versión en miniatura de la imagen
     */
    fun getThumbnailUrl(): String =
        url.replace(".jpg", "_thumb.jpg")
            .replace(".png", "_thumb.png")
            .replace(".jpeg", "_thumb.jpeg")

}