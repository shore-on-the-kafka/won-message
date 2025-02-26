package com.won.message.controller

import com.won.message.controller.request.MessageCreateReqeustBody
import com.won.message.exception.MessageException
import com.won.message.message.Message
import com.won.message.message.MessageService
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
class V1MessageController(
    private val messageService: MessageService,
) {

    @PostMapping("/v1/spaces/{spaceId}/messages")
    fun create(
        @PathVariable spaceId: String,
        @RequestBody body: MessageCreateReqeustBody,
    ): Message {
        val message = Message.create(spaceId, body, Instant.now())
        return messageService.create(message)
    }

    @GetMapping("/v1/spaces/{spaceId}/messages/{messageId}")
    fun get(
        @PathVariable spaceId: String,
        @PathVariable messageId: String,
    ): Message {
        return kotlin.runCatching { messageService.getOrException(spaceId, messageId) }
            .getOrElse {
                when (it) {
                    is IllegalArgumentException -> throw MessageException.MessageNotFoundException()
                    else -> throw it
                }
            }
    }

    @GetMapping("/v1/spaces/{spaceId}/messages")
    fun getList(@PathVariable spaceId: String): List<Message> {
        return messageService.getListBySpaceId(spaceId)
    }

    /**
     * This API is a temporary API.
     * If a Batch request API is added later, This API will be deleted.
     */
    @PostMapping("/v1/messages")
    fun getListBySpaceIds(@RequestBody spaceIds: List<String>): List<Message> {
        return spaceIds.map { messageService.getListBySpaceId(it) }
            .flatten()
            .sortedByDescending { it.createTime }
    }

    @DeleteMapping("/v1/spaces/{spaceId}/messages/{messageId}")
    fun deleteById(
        @PathVariable spaceId: String,
        @PathVariable messageId: String,
    ) {
        messageService.deleteById(spaceId, messageId)
    }

}
