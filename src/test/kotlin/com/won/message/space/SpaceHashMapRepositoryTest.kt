package com.won.message.space

import com.won.message.TestObjectFactory
import com.won.message.config.HashMapRepositoryConfiguration
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SpaceHashMapRepositoryTest {
    private lateinit var repository: HashMapRepositoryConfiguration.SpaceHashMapRepository

    @BeforeTest
    fun setUp() {
        repository = HashMapRepositoryConfiguration.SpaceHashMapRepository()
    }

    @Test
    fun `create and get space`() {
        val space = TestObjectFactory.createSpace()
        val before = repository.get(space.id)

        repository.create(space)
        val after = repository.get(space.id)

        assertNull(before)
        assertEquals(space, after)
    }

    @Test
    fun `update existing space`() {
        val existingSpace = TestObjectFactory.createSpace()
        repository.create(existingSpace)

        val updatedSpace = TestObjectFactory.createSpace(spaceId = existingSpace.id)

        val updateResult = repository.update(updatedSpace)
        val after = repository.get(existingSpace.id)

        assertEquals(updatedSpace, updateResult)
        assertEquals(updatedSpace, after)
    }

    @Test
    fun `delete space by id`() {
        val space = TestObjectFactory.createSpace()
        repository.create(space)

        repository.deleteById(space.id)
        val result = repository.get(space.id)

        assertNull(result)
    }

}
