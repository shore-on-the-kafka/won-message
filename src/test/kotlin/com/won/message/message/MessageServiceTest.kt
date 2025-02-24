package com.won.message.message

import com.won.message.TestObjectFactory.createMessage
import io.mockk.*
import kotlin.test.Test
import kotlin.test.assertEquals

class MessageServiceTest {

    private val repository = mockk<MessageRepository>()
    private val cut = MessageService(repository)

    private val message = createMessage()

    @Test
    fun create() {
        every { repository.create(message) } returns message

        val result = cut.create(message)

        assertEquals(result, message)
        verify(exactly = 1) { repository.create(message) }
    }

    @Test
    fun get() {
        every { repository.getOrNull(message.id) } returns message

        val result = cut.getOrException(message.spaceId, message.id)

        assertEquals(result, message)
        verify(exactly = 1) { repository.getOrNull(message.id) }
    }

    @Test
    fun update() {
        val updateMessage = createMessage(message.spaceId, message.id)
        every { repository.update(updateMessage) } returns updateMessage

        val result = cut.update(updateMessage)

        assertEquals(result, updateMessage)
        verify(exactly = 1) { repository.update(updateMessage) }
    }

    @Test
    fun deleteById() {
        every { repository.deleteById(message.id) } just runs

        cut.deleteById(message.spaceId, message.id)

        verify(exactly = 1) { repository.deleteById(message.id) }
    }

}
