package com.jmfg.certs.dh.prodev.model

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PreUpdate
import java.time.LocalDateTime

@MappedSuperclass
abstract class BaseEntity {

    @Column(nullable = false, updatable = false)
    val creationDate: LocalDateTime = LocalDateTime.now()

    @Column(nullable = false)
    var updateDate: LocalDateTime? = null
        private set

    @PreUpdate
    protected fun onUpdate() {
        updateDate = LocalDateTime.now()
    }
}

