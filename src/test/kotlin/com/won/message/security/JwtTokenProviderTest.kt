package com.won.message.security

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class JwtTokenProviderTest {

    private lateinit var jwtTokenProvider: JwtTokenProvider

    @BeforeEach
    fun setUp() {
        val secretKey = "default-secret-key-which-is-long-enough-for-testing"
        jwtTokenProvider = JwtTokenProvider(secretKey, 600000)
    }

    @Test
    fun `createToken - 토큰 생성 테스트`() {
        val username = "testUser"
        val roles = listOf("USER", "ADMIN")

        val token = jwtTokenProvider.createToken(username, roles)

        assertNotNull(token)
        assertTrue(token.isNotBlank())
    }

    @Test
    fun `validateToken - 유효한 토큰 테스트`() {
        val username = "testUser"
        val roles = listOf("USER", "ADMIN")
        val token = jwtTokenProvider.createToken(username, roles)

        val isValid = jwtTokenProvider.validateToken(token)

        assertTrue(isValid)
    }

    @Test
    fun `validateToken - 잘못된 토큰 테스트`() {
        val invalidToken = "invalid.token.value"

        val isValid = jwtTokenProvider.validateToken(invalidToken)

        assertFalse(isValid)
    }

    @Test
    fun `getUsername - 토큰에서 사용자 이름 추출 테스트`() {
        val username = "testUser"
        val roles = listOf("USER", "ADMIN")
        val token = jwtTokenProvider.createToken(username, roles)

        val extractedUsername = jwtTokenProvider.getUsername(token)

        assertEquals(username, extractedUsername)
    }

    @Test
    fun `getRoles - 토큰에서 역할 추출 테스트`() {
        val username = "testUser"
        val roles = listOf("USER", "ADMIN")
        val token = jwtTokenProvider.createToken(username, roles)

        val extractedRoles = jwtTokenProvider.getRoles(token)

        assertEquals(roles, extractedRoles)
    }

    @Test
    fun `validateToken - 만료된 토큰 테스트`() {
        val username = "testUser"
        val roles = listOf("USER", "ADMIN")
        val expiredJwtTokenProvider = JwtTokenProvider("", 1) // 1ms 만료 시간
        val token = expiredJwtTokenProvider.createToken(username, roles)

        Thread.sleep(3) // 토큰 만료 대기

        val isValid = expiredJwtTokenProvider.validateToken(token)

        assertFalse(isValid)
    }
}
