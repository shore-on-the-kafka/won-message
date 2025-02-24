package com.won.message.message

import com.won.message.TestObjectFactory
import com.won.message.config.HashMapRepositoryConfiguration
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MessageHashMapRepositoryTest {

    private lateinit var repository: HashMapRepositoryConfiguration.MessageHashMapRepository

    @BeforeTest
    fun setUp() {
        repository = HashMapRepositoryConfiguration.MessageHashMapRepository()
    }

    @Test
    fun `create and get message`() {
        val message = TestObjectFactory.createMessage()
        val before = repository.getOrNull(message.id)

        repository.create(message)
        val after = repository.getOrNull(message.id)

        assertNull(before)
        assertEquals(message, after)
    }

    @Test
    fun `update existing message`() {
        val existingMessage = TestObjectFactory.createMessage()
        repository.create(existingMessage)

        val updatedMessage = TestObjectFactory.createMessage(messageId = existingMessage.id)

        val updateResult = repository.update(updatedMessage)
        val after = repository.getOrNull(existingMessage.id)

        assertEquals(updatedMessage, updateResult)
        assertEquals(updatedMessage, after)
    }

    @Test
    fun `delete message by id`() {
        val message = TestObjectFactory.createMessage()
        repository.create(message)

        repository.deleteById(message.id)
        val result = repository.getOrNull(message.id)

        assertNull(result)
    }

    @Test
    fun `getListBySpaceId should return messages sorted by createTime desc`() {
        val spaceId = TestObjectFactory.createSpaceId()
        val messageBuilder = TestObjectFactory.getMessageFixtureBuilder()
        val firstMessage = messageBuilder.set(Message::spaceId, spaceId).build().sample()
        val secondMessage = messageBuilder.set(Message::spaceId, spaceId)
            .set(Message::createTime, firstMessage.createTime.plusSeconds(10L)).build().sample()
        val anotherSpaceMessage = TestObjectFactory.createMessage()
        repository.create(firstMessage)
        repository.create(secondMessage)
        repository.create(anotherSpaceMessage)

        val result = repository.getListBySpaceId(spaceId)

        assertEquals(2, result.size)
        assertEquals(secondMessage, result[0])
        assertEquals(firstMessage, result[1])
    }

}
