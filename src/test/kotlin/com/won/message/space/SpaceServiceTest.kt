package com.won.message.space

import com.won.message.TestObjectFactory
import com.won.message.user.UserService
import io.mockk.*
import kotlin.test.Test
import kotlin.test.assertEquals

class SpaceServiceTest {

    private val userService = mockk<UserService>(relaxed = true)
    private val repository = mockk<SpaceRepository>()
    private val cut = SpaceService(userService, repository)

    private val space = TestObjectFactory.createSpace()

    @Test
    fun create() {
        every { repository.create(space) } returns space

        val result = cut.create(space)

        assertEquals(result, space)
        verify(exactly = 1) { repository.create(space) }
        verify(exactly = space.getJoinedUserIds().size) { userService.addJoinedSpaceId(any(), eq(space.id)) }
    }

    @Test
    fun get() {
        every { repository.get(space.id) } returns space

        val result = cut.getOrException(space.id)

        assertEquals(result, space)
        verify(exactly = 1) { repository.get(space.id) }
    }

    @Test
    fun update() {
        val updateSpace = TestObjectFactory.createSpace(space.id)
        every { repository.update(updateSpace) } returns updateSpace

        val result = cut.update(updateSpace)

        assertEquals(result, updateSpace)
        verify(exactly = 1) { repository.update(updateSpace) }
    }

    @Test
    fun deleteById() {
        every { repository.deleteById(space.id) } just runs

        cut.deleteById(space.id)

        verify(exactly = 1) { repository.deleteById(space.id) }
    }

}
