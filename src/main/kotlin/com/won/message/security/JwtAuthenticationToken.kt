package com.won.message.security

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class JwtAuthenticationToken(
    private val principal: String,
    private val credentials: Any? = null,
    private val authorities: Collection<GrantedAuthority> = emptyList(),
    private val isAuthenticated: Boolean = false,
) : AbstractAuthenticationToken(authorities) {
    override fun getCredentials(): Any? = credentials
    override fun getPrincipal(): Any = principal
}
