package com.won.message.message

import org.springframework.stereotype.Service

@Service
class MessageService(
    private val messageRepository: MessageRepository,
) {
    fun create(entity: Message): Message {
        return messageRepository.create(entity)
    }

    /**
     * @throws IllegalArgumentException if message not found
     */
    fun getOrException(spaceId: String, id: String): Message {
        val message = messageRepository.get(id)
        require(message?.spaceId == spaceId) { "Message not found" }
        return message!!
    }

    fun getListBySpaceId(spaceId: String): List<Message> {
        return messageRepository.getListBySpaceId(spaceId)
    }

    fun update(entity: Message): Message {
        return messageRepository.update(entity)
    }

    fun deleteById(spaceId: String, id: String) {
        messageRepository.deleteById(id)
    }
}
