package com.won.message.security

import com.fasterxml.jackson.databind.ObjectMapper

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class JwtLoginAuthenticationFilter(
    authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider,
    private val objectMapper: ObjectMapper
) : UsernamePasswordAuthenticationFilter(authenticationManager) {

    init {
        setFilterProcessesUrl("/login")
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication,
    ) {
        val username = authResult.name
        val roles = authResult.authorities.map { it.authority }
        val token = jwtTokenProvider.createToken(username, roles)

        response.contentType = "application/json"
        response.writer.write(objectMapper.writeValueAsString(mapOf("token" to token)))
    }

}
