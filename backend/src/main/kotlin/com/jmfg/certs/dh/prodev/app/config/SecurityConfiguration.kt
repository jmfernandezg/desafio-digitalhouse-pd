package com.jmfg.certs.dh.prodev.app.config

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

@Configuration
class SecurityConfiguration {
    @Bean
    fun jwtEncoder(keyPair: KeyPair): JwtEncoder {
        val jwk = RSAKey.Builder(keyPair.public as RSAPublicKey)
            .privateKey(keyPair.private as RSAPrivateKey)
            .algorithm(JWSAlgorithm.RS256)
            .build()
        val jwks = ImmutableJWKSet<SecurityContext>(JWKSet(jwk))
        return NimbusJwtEncoder(jwks)
    }

    @Bean
    @Profile("dev")
    fun keyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(2048)
        return keyPairGenerator.generateKeyPair()
    }

}