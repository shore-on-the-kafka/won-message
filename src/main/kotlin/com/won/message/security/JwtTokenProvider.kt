package com.won.message.security

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.SecureRandom
import java.util.*

@Component
class JwtTokenProvider(
    @Value("\${security.jwt.secret-key:}") private val secretKey: String,
    @Value("\${security.jwt.expiration-millisec:600000}") private val expiration: Long,
) {
    private val key = if (secretKey.isBlank()) {
        val defaultSecretKey = ByteArray(64)
        SecureRandom().nextBytes(defaultSecretKey)
        Keys.hmacShaKeyFor(defaultSecretKey)
    } else {
        Keys.hmacShaKeyFor(secretKey.toByteArray())
    }
    private val jwtParser = Jwts.parser().verifyWith(key).build()

    fun createToken(username: String, roles: List<String>): String {
        val now = Date()
        val expiration = Date(now.time + expiration)

        return Jwts.builder()
            .subject(username)
            .claim("roles", roles)
            .issuedAt(now)
            .expiration(expiration)
            .signWith(key)
            .compact()
    }

    fun validateToken(token: String): Boolean = try {
        jwtParser.parseSignedClaims(token)
        true
    } catch (ex: JwtException) {
        false
    }

    fun getUsername(token: String): String = jwtParser.parseSignedClaims(token).payload.subject

    fun getRoles(token: String): List<String> =
        (jwtParser.parseSignedClaims(token).payload["roles"] as? List<*>)?.map { it.toString() } ?: emptyList()
}
