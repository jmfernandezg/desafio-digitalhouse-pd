package com.jmfg.certs.dh.prodev.app.config

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.time.Instant

@Configuration
class SecurityConfig {
    val logger: Logger = LoggerFactory.getLogger(SecurityConfig::class.java)

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun jwtEncoder(keyPair: KeyPair): JwtEncoder {
        return NimbusJwtEncoder(
            ImmutableJWKSet(
                JWKSet(
                    RSAKey.Builder(keyPair.public as RSAPublicKey).privateKey(keyPair.private as RSAPrivateKey)
                        .algorithm(JWSAlgorithm.RS256).build()
                )
            )
        )
    }

    @Bean
    @Profile("dev")
    fun keyPair(): KeyPair = KeyPairGenerator.getInstance("RSA").apply {
        initialize(2048)
    }.generateKeyPair()


    @Bean
    @Profile("dev")
    fun testJwtTokenCreation(jwtEncoder: JwtEncoder) = CommandLineRunner {
        JwtClaimsSet.builder().issuer("test-issuer").subject("test-subject").issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600)).claim("scope", "test-scope").build().run {
                jwtEncoder.encode(JwtEncoderParameters.from(this)).tokenValue.also {
                    logger.info("Generated dev token: $it")
                }
            }
    }
}

