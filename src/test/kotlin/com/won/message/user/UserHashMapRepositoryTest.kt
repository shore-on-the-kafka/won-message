package com.won.message.user

import com.won.message.TestObjectFactory
import com.won.message.config.HashMapRepositoryConfiguration
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UserHashMapRepositoryTest {
    private lateinit var repository: HashMapRepositoryConfiguration.UserHashMapRepository

    @BeforeTest
    fun setUp() {
        repository = HashMapRepositoryConfiguration.UserHashMapRepository()
    }

    @Test
    fun `create and get user`() {
        val user = TestObjectFactory.createUser()
        val before = repository.get(user.id)

        repository.create(user)
        val after = repository.get(user.id)

        assertNull(before)
        assertEquals(user, after)
    }

}
