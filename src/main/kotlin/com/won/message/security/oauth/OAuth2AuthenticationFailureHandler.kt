package com.won.message.security.oauth

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler

class OAuth2AuthenticationFailureHandler : AuthenticationFailureHandler {

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException,
    ) {
        println("Fail to Oauth login. ${exception.message}")

        response.status = HttpServletResponse.SC_BAD_REQUEST
        response.writer.write("Fail to Oauth login.")
    }

}
