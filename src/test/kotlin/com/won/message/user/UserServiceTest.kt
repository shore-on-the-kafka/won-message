package com.won.message.user

import com.won.message.TestObjectFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.security.crypto.password.PasswordEncoder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UserServiceTest {
    private val repository = mockk<UserRepository>()
    private val passwordEncoder = mockk<PasswordEncoder>()
    private val cut = UserService(repository, passwordEncoder)

    private val user = TestObjectFactory.createUser()

    @Test
    fun create() {
        val rawPassword = user.password
        val encodedPassword = "encodedPassword"

        every { passwordEncoder.encode(rawPassword) } returns encodedPassword
        every { repository.create(any()) } answers { firstArg() }

        val result = cut.create(user)

        assertEquals(encodedPassword, result.password)
        verify(exactly = 1) { passwordEncoder.encode(rawPassword) }
        verify(exactly = 1) { repository.create(any()) }
    }

    @Test
    fun `createIfNotExists - user does not exist`() {
        val rawPassword = user.password
        val encodedPassword = "encodedPassword"

        every { repository.get(user.id) } returns null
        every { passwordEncoder.encode(rawPassword) } returns encodedPassword
        every { repository.create(any()) } answers { firstArg() }

        val result = cut.createIfNotExists(user)

        assertEquals(user.name, result.name)
        assertEquals(encodedPassword, result.password)
        verify(exactly = 1) { repository.get(user.id) }
        verify(exactly = 1) { passwordEncoder.encode(rawPassword) }
        verify(exactly = 1) { repository.create(any()) }
    }

    @Test
    fun `createIfNotExists - user already exists`() {
        every { repository.get(user.id) } returns user

        val result = cut.createIfNotExists(user)

        assertEquals(user, result)
        verify(exactly = 1) { repository.get(user.id) }
        verify(exactly = 0) { repository.create(any()) }
    }

    @Test
    fun get() {
        every { repository.get(user.id) } returns user

        val result = cut.getOrException(user.id)

        assertEquals(result, user)
        verify(exactly = 1) { repository.get(user.id) }
    }

    @Test
    fun `user not found throw exception`() {
        every { repository.get(user.id) } returns null

        assertFailsWith<IllegalArgumentException> { cut.getOrException(user.id) }
        verify(exactly = 1) { repository.get(user.id) }
    }
}
