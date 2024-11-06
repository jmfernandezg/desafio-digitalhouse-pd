package com.jmfg.certs.dh.prodev.model

import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import java.time.LocalDateTime
import jakarta.persistence.Column
import jakarta.persistence.OneToMany

@MappedSuperclass
abstract class BaseEntity {

    @Column(nullable = false, updatable = false)
    var creationDate: LocalDateTime = LocalDateTime.now()

    @Column(nullable = false)
    var updateDate: LocalDateTime? = null
        private set

    @PrePersist
    protected fun onCreate() {
        creationDate = LocalDateTime.now()
        updateDate = LocalDateTime.now()
    }

    @PreUpdate
    protected fun onUpdate() {
        updateDate = LocalDateTime.now()
    }
}

