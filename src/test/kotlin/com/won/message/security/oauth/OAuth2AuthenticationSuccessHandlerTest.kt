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
import org.springframework.security.oauth2.core.oidc.user.OidcUser
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
    fun `OIDC OAuth2 SuccessHandler should create token and write to response`() {
        val oidcSubject = "oidc-subject-123"
        val userGivenName = "Test"
        val userNickName = "TestUser"
        val providerId = "google"
        val userIdentifier = "${providerId}_${oidcSubject}"
        val loginUser =
            User.createOAuth2LoginUser(id = userIdentifier, name = userGivenName, requestTime = Instant.now())
        val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))

        val oidcUser = mockk<OidcUser> {
            every { subject } returns oidcSubject
            every { givenName } returns userGivenName
            every { nickName } returns userNickName
        }
        val authentication = OAuth2AuthenticationToken(oidcUser, authorities, providerId)

        every { userService.createIfNotExists(any()) } returns loginUser
        every { jwtTokenProvider.createToken(loginUser.name, listOf("ROLE_USER")) } returns "mocked-jwt-token"

        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()

        successHandler.onAuthenticationSuccess(request, response, authentication)

        val responseJson = objectMapper.readTree(response.contentAsString)

        assertEquals("mocked-jwt-token", responseJson["token"].asText())
        assertEquals(loginUser.id.value, responseJson["loginUser"]["id"].asText())
        assertEquals(loginUser.name, responseJson["loginUser"]["name"].asText())
        assertEquals(HttpServletResponse.SC_OK, response.status)
    }

}
