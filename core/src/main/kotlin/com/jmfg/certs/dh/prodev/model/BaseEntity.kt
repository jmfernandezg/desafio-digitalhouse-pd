package com.jmfg.certs.dh.prodev.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * Clase base para entidades del sistema
 *
 * Esta clase abstracta proporciona funcionalidad común para todas las entidades
 * del sistema, incluyendo:
 * - Auditoría de fechas (creación y actualización)
 * - Métodos de utilidad para manejo de tiempo
 * - Configuración base de JPA
 *
 * Todas las entidades del sistema deben heredar de esta clase para mantener
 * consistencia en el manejo de auditoría.
 */
@MappedSuperclass
abstract class BaseEntity {

    /**
     * Fecha y hora de creación de la entidad
     *
     * Este valor se establece automáticamente al persistir la entidad
     * y no puede ser modificado posteriormente.
     */
    @Column(nullable = false, updatable = false)
    @JsonIgnore
    private var _creationDate: LocalDateTime? = null

    /**
     * Fecha y hora de la última actualización
     *
     * Este valor se actualiza automáticamente cada vez que la entidad
     * es modificada.
     */
    @Column
    @JsonIgnore
    private var _updateDate: LocalDateTime? = null

    /**
     * Obtiene la fecha de creación
     *
     * @throws IllegalStateException si la entidad no ha sido persistida
     */
    val creationDate: LocalDateTime
        get() = _creationDate ?: throw IllegalStateException(
            "La fecha de creación no está disponible hasta que la entidad sea persistida"
        )

    /**
     * Obtiene la fecha de última actualización
     */
    val updateDate: LocalDateTime?
        get() = _updateDate

    /**
     * Calcula el tiempo transcurrido desde la creación
     *
     * @return Duración en días desde la creación
     * @throws IllegalStateException si la entidad no ha sido persistida
     */
    fun daysSinceCreation(): Long =
        ChronoUnit.DAYS.between(creationDate, LocalDateTime.now())

    /**
     * Calcula el tiempo transcurrido desde la última actualización
     *
     * @return Duración en días desde la última actualización, o null si nunca se actualizó
     */
    fun daysSinceLastUpdate(): Long? =
        updateDate?.let { ChronoUnit.DAYS.between(it, LocalDateTime.now()) }

    /**
     * Verifica si la entidad ha sido modificada
     *
     * @return true si la entidad ha sido actualizada al menos una vez
     */
    fun hasBeenModified(): Boolean = _updateDate != null

    /**
     * Verifica si la entidad es reciente
     *
     * @param days Número de días para considerar una entidad como reciente
     * @return true si la entidad fue creada dentro del período especificado
     */
    fun isRecent(days: Long = 7): Boolean =
        daysSinceCreation() <= days

    /**
     * Callback ejecutado antes de persistir la entidad
     */
    @PrePersist
    protected fun onCreate() {
        _creationDate = LocalDateTime.now()
    }

    /**
     * Callback ejecutado antes de actualizar la entidad
     */
    @PreUpdate
    protected fun onUpdate() {
        _updateDate = LocalDateTime.now()
    }

    companion object {
        /**
         * Cantidad predeterminada de días para considerar una entidad como reciente
         */
        const val DEFAULT_RECENT_DAYS = 7L

        /**
         * Formatea una fecha para mostrar
         *
         * @param dateTime Fecha a formatear
         * @return String con la fecha formateada
         */
        fun formatDateTime(dateTime: LocalDateTime): String =
            dateTime.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    /**
     * Obtiene información de auditoría de la entidad
     *
     * @return Map con información de auditoría
     */
    fun getAuditInfo(): Map<String, String?> = mapOf(
        "creationDate" to _creationDate?.let { formatDateTime(it) },
        "updateDate" to _updateDate?.let { formatDateTime(it) },
        "daysSinceCreation" to daysSinceCreation().toString(),
        "daysSinceLastUpdate" to daysSinceLastUpdate()?.toString(),
        "hasBeenModified" to hasBeenModified().toString()
    )
}