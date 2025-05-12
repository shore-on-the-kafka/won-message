package com.won.message.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.won.message.security.*
import com.won.message.security.oauth.OAuth2AuthenticationFailureHandler
import com.won.message.security.oauth.OAuth2AuthenticationSuccessHandler
import com.won.message.user.UserDetailsService
import com.won.message.user.UserService
import jakarta.servlet.DispatcherType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.DispatcherTypeRequestMatcher

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider,
) {

    @Bean
    fun filterChain(
        userService: UserService,
        authenticationManager: AuthenticationManager,
        objectMapper: ObjectMapper,
        http: HttpSecurity,
    ): SecurityFilterChain {
        val loginAuthFilter =
            JwtLoginAuthenticationFilter(authenticationManager, jwtTokenProvider, objectMapper)
        val jwtAuthFilter = JwtAuthenticationFilter(authenticationManager)

        http {
            csrf { disable() }
            authorizeHttpRequests {
                authorize(DispatcherTypeRequestMatcher(DispatcherType.ERROR), permitAll)
                authorize("/oauth2/**", "/login/oauth2/**", permitAll)
                authorize(HttpMethod.POST, "/v1/users", permitAll)
                authorize("/v1/**", hasRole(Roles.USER))
            }
            oauth2Login {
                authenticationSuccessHandler =
                    OAuth2AuthenticationSuccessHandler(userService, jwtTokenProvider, objectMapper)
                authenticationFailureHandler = OAuth2AuthenticationFailureHandler()
            }
            addFilterAt<UsernamePasswordAuthenticationFilter>(loginAuthFilter)
            addFilterBefore<UsernamePasswordAuthenticationFilter>(jwtAuthFilter)
        }
        return http.build()
    }

    @Bean
    fun authenticationManager(userDetailsService: UserDetailsService): AuthenticationManager {
        return ProviderManager(
            listOf(
                // For login
                DaoAuthenticationProvider().apply {
                    setUserDetailsService(userDetailsService)
                    setPasswordEncoder(passwordEncoder())
                },
                // For JWT
                JwtAuthenticationProvider(jwtTokenProvider)
            )
        )
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }
}
