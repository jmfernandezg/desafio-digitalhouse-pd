package com.jmfg.certs.dh.prodev.app.config

import com.nimbusds.jose.jwk.source.ImmutableSecret
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import java.util.*
import javax.crypto.spec.SecretKeySpec

@Configuration
@EntityScan("com.jmfg.certs.dh.prodev.model")
@EnableJpaRepositories("com.jmfg.certs.dh.prodev.app.repository")
class BackendConfig {

    @Value("\${jwt.secret}")
    private val jwtSecret: String? = null

    @Bean
    fun jwtEncoder(): JwtEncoder = NimbusJwtEncoder(
        ImmutableSecret(
            SecretKeySpec(
                Base64.getDecoder().decode(jwtSecret), "HmacSHA256"
            )
        )
    )

}