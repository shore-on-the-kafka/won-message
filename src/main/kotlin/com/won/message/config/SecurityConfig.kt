package com.won.message.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.won.message.security.*
import com.won.message.user.UserDetailsService
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

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider,
) {

    @Bean
    fun filterChain(
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
                authorize(HttpMethod.POST, "/v1/users", permitAll)
                authorize("/v1/**", hasRole(Roles.USER))
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
