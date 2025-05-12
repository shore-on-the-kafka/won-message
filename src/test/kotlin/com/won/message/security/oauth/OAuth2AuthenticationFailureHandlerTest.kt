package com.won.message.security.oauth

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.authentication.AuthenticationServiceException

class OAuth2AuthenticationFailureHandlerTest {

    private val failureHandler = OAuth2AuthenticationFailureHandler()

    @Test
    fun `OAuth2 로그인 실패 시 400 상태와 에러 메시지를 반환한다`() {
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        val exception = AuthenticationServiceException("Invalid OAuth2 state")

        failureHandler.onAuthenticationFailure(request, response, exception)

        assertEquals(400, response.status)
        assertEquals("Fail to Oauth login.", response.contentAsString.trim())
    }

}
