package com.won.message.security.oauth

import com.fasterxml.jackson.databind.ObjectMapper
import com.won.message.security.JwtTokenProvider
import com.won.message.user.User
import com.won.message.user.UserService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import java.time.Instant

class OAuth2AuthenticationSuccessHandler(
    private val userService: UserService,
    private val jwtTokenProvider: JwtTokenProvider,
    private val objectMapper: ObjectMapper,
) : AuthenticationSuccessHandler {

    private val providerPkAttributeMap = mapOf(
        "google" to "email"
    )

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,
    ) {
        if (authentication !is OAuth2AuthenticationToken) {
            throw IllegalStateException("Authentication is not OAuth2AuthenticationToken")
        }

        val provider = authentication.authorizedClientRegistrationId
        val pkAttribute = providerPkAttributeMap[provider]?.let { pkAttributeName ->
            authentication.principal.getAttribute<String>(pkAttributeName)
        } ?: throw IllegalStateException("Fail to get pk attribute. registrationId:  $provider")

        val loginUser = userService.createIfNotExists(
            user = User.createOAuth2LoginUser(name = pkAttribute, requestTime = Instant.now())
        )

        // TODO: Add roles on User entity. (Using [authentication.authorities])
        val token = jwtTokenProvider.createToken(pkAttribute, listOf("ROLE_USER"))

        val responseBody = mapOf(
            "loginUser" to loginUser,
            "token" to token
        )
        response.contentType = "application/json"
        response.writer.write(objectMapper.writeValueAsString(responseBody))
    }
}
