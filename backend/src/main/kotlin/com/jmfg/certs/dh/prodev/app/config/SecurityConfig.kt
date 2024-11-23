package com.jmfg.certs.dh.prodev.app.config

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
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

/**
 * Configuración de seguridad para la aplicación
 *
 * Esta clase maneja la configuración de seguridad, incluyendo:
 * - Codificación de contraseñas
 * - Generación y codificación de tokens JWT
 * - Configuración de pares de claves RSA
 *
 * @property logger Registrador para eventos de seguridad
 */
@Configuration
class SecurityConfig {
    companion object {
        private const val KEY_SIZE = 2048
        private const val TOKEN_VALIDITY_SECONDS = 3600L
        private const val RSA_ALGORITHM = "RSA"
    }

    private val logger: Logger = getLogger(SecurityConfig::class.java)

    /**
     * Proporciona el codificador de contraseñas para la aplicación
     *
     * @return Instancia de BCryptPasswordEncoder para el hash seguro de contraseñas
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    /**
     * Configura el codificador JWT utilizando claves RSA
     *
     * @param keyPair Par de claves RSA para firmar y verificar tokens
     * @return Codificador JWT configurado
     */
    @Bean
    fun jwtEncoder(keyPair: KeyPair): JwtEncoder {
        val rsaKey = RSAKey.Builder(keyPair.public as RSAPublicKey).privateKey(keyPair.private as RSAPrivateKey)
            .algorithm(JWSAlgorithm.RS256).build()

        return JWKSet(rsaKey).run {
            NimbusJwtEncoder(ImmutableJWKSet(this))
        }
    }

    /**
     * Genera un par de claves RSA para el ambiente de desarrollo
     *
     * @return Par de claves RSA de 2048 bits
     */
    @Bean
    @Profile("dev")
    fun keyPair(): KeyPair = KeyPairGenerator.getInstance(RSA_ALGORITHM).apply {
        initialize(KEY_SIZE)
    }.generateKeyPair()

    /**
     * Genera un token JWT de prueba durante el inicio de la aplicación en ambiente de desarrollo
     *
     * @param jwtEncoder Codificador JWT configurado
     * @return CommandLineRunner que genera y registra un token de prueba
     */
    @Bean
    @Profile("dev")
    fun testJwtTokenCreation(jwtEncoder: JwtEncoder) = CommandLineRunner {
        val claims =
            JwtClaimsSet.builder().issuer("test-dev-issuer").subject("test-dev-subject").issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(TOKEN_VALIDITY_SECONDS)).claim("scope", "test-dev-scope").build()

        jwtEncoder.encode(JwtEncoderParameters.from(claims)).tokenValue.also {
            logger.info("Token de desarrollo generado: $it")
        }
    }
}
