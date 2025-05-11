package com.won.message.security

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority

class JwtAuthenticationProvider(
    private val jwtTokenProvider: JwtTokenProvider,
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {
        val token = authentication.principal as String
        if (!jwtTokenProvider.validateToken(token)) {
            throw BadCredentialsException("Invalid JWT token")
        }

        val username = jwtTokenProvider.getUsername(token)
        val roles = jwtTokenProvider.getRoles(token)
        val authorities = roles.map { SimpleGrantedAuthority(it) }

        return JwtAuthenticationToken(
            principal = username,
            authorities = authorities,
            isAuthenticated = true
        )
    }

    override fun supports(authentication: Class<*>): Boolean {
        return JwtAuthenticationToken::class.java.isAssignableFrom(authentication)
    }
}
