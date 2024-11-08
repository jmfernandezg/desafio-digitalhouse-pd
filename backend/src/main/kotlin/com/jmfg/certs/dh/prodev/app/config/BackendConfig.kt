package com.jmfg.certs.dh.prodev.app.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EntityScan("com.jmfg.certs.dh.prodev.model")
@EnableJpaRepositories("com.jmfg.certs.dh.prodev.app.repository")
class BackendConfig {
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}