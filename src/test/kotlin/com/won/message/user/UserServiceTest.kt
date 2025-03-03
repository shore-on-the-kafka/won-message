package com.won.message.user

import com.won.message.TestObjectFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UserServiceTest {
    private val repository = mockk<UserRepository>()
    private val cut = UserService(repository)

    private val user = TestObjectFactory.createUser()

    @Test
    fun create() {
        every { repository.create(user) } returns user

        val result = cut.create(user)

        assertEquals(result, user)
        verify(exactly = 1) { repository.create(user) }
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
