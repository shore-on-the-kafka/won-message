package com.won.message.security.oauth

import com.fasterxml.jackson.databind.ObjectMapper
import com.won.message.security.JwtTokenProvider
import com.won.message.user.User
import com.won.message.user.UserService
import io.mockk.every
import io.mockk.mockk
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import java.time.Instant

@SpringBootTest
class OAuth2AuthenticationSuccessHandlerTest @Autowired constructor(
    private val objectMapper: ObjectMapper,
) {

    private val jwtTokenProvider = mockk<JwtTokenProvider>()
    private val userService = mockk<UserService>()
    private val successHandler = OAuth2AuthenticationSuccessHandler(
        userService = userService,
        jwtTokenProvider = jwtTokenProvider,
        objectMapper = objectMapper
    )

    @Test
    fun `Google OAuth2 SuccessHandler should create token and write to response`() {
        val googleLoginEmail = "test@example.com"
        val loginUser = User.createOAuth2LoginUser(name = googleLoginEmail, requestTime = Instant.now())

        val principal = DefaultOAuth2User(
            listOf(SimpleGrantedAuthority("ROLE_USER")),
            mapOf("email" to googleLoginEmail),
            "email"
        )
        val authentication = OAuth2AuthenticationToken(principal, principal.authorities, "google")

        every { userService.createIfNotExists(any()) } returns loginUser
        every { jwtTokenProvider.createToken(googleLoginEmail, listOf("ROLE_USER")) } returns "mocked-jwt-token"

        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()

        successHandler.onAuthenticationSuccess(request, response, authentication)

        val responseJson = objectMapper.readTree(response.contentAsString)

        assertEquals("mocked-jwt-token", responseJson["token"].asText())
        assertEquals(loginUser.name, responseJson["loginUser"]["name"].asText())
        assertEquals(HttpServletResponse.SC_OK, response.status)
    }

}
