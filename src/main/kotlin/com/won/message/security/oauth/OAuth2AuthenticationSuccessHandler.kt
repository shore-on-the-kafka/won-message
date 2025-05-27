package com.won.message.security.oauth

import com.fasterxml.jackson.databind.ObjectMapper
import com.won.message.security.JwtTokenProvider
import com.won.message.user.User
import com.won.message.user.UserService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import java.time.Instant

class OAuth2AuthenticationSuccessHandler(
    private val userService: UserService,
    private val jwtTokenProvider: JwtTokenProvider,
    private val objectMapper: ObjectMapper,
) : AuthenticationSuccessHandler {
    private val ID_DELIMITER = "_"
    private val UNKNOWN_USER_NAME = "Unknown User"

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,
    ) {
        if (authentication !is OAuth2AuthenticationToken) {
            throw IllegalStateException("Authentication is not OAuth2AuthenticationToken")
        }

        val principal = authentication.principal
        val provider = authentication.authorizedClientRegistrationId
        val userIdentifierPrefix = provider + ID_DELIMITER
        val userIdentifier: String
        val userName: String

        when (principal) {
            is OidcUser -> {
                // For OIDC providers like Google, GitHub, etc.
                userIdentifier = userIdentifierPrefix + principal.subject
                userName = principal.givenName ?: principal.nickName ?: UNKNOWN_USER_NAME
                if (userName == UNKNOWN_USER_NAME) {
                    println("Warning: User name is not provided by OIDC provider($provider). $principal")
                }
            }

            else -> throw IllegalStateException("Is not supported principal type: ${principal::class.java}")
        }

        val loginUser = userService.createIfNotExists(
            user = User.createOAuth2LoginUser(id = userIdentifier, name = userName, requestTime = Instant.now())
        )

        // TODO: Add roles on User entity. (Using [authentication.authorities])
        val token = jwtTokenProvider.createToken(loginUser.name, listOf("ROLE_USER"))

        val responseBody = mapOf(
            "loginUser" to loginUser,
            "token" to token
        )
        response.contentType = "application/json"
        response.writer.write(objectMapper.writeValueAsString(responseBody))
    }
}
