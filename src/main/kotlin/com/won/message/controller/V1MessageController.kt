package com.won.message.controller

import com.won.message.controller.request.MessageCreateReqeustBody
import com.won.message.message.Message
import com.won.message.message.MessageService
import org.springframework.http.ResponseEntity
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
    ): ResponseEntity<Message> {
        val message = Message.create(spaceId, body, Instant.now())
        return ResponseEntity.ok(messageService.create(message))
    }

    @GetMapping("/v1/spaces/{spaceId}/messages/{messageId}")
    fun get(
        @PathVariable spaceId: String,
        @PathVariable messageId: String,
    ): ResponseEntity<Message> {
        return kotlin.runCatching { ResponseEntity.ok(messageService.getOrException(spaceId, messageId)) }
            .getOrElse {
                when (it) {
                    is IllegalArgumentException -> ResponseEntity.notFound().build()
                    else -> throw it
                }
            }
    }

    @GetMapping("/v1/spaces/{spaceId}/messages")
    fun getList(@PathVariable spaceId: String): ResponseEntity<List<Message>> {
        return ResponseEntity.ok(messageService.getListBySpaceId(spaceId))
    }

    /**
     * This API is a temporary API.
     * If a Batch request API is added later, This API will be deleted.
     */
    @PostMapping("/v1/messages")
    fun getListBySpaceIds(@RequestBody spaceIds: List<String>): ResponseEntity<List<Message>> {
        return ResponseEntity.ok(spaceIds.map { messageService.getListBySpaceId(it) }
            .flatten()
            .sortedByDescending { it.createTime })
    }

    @DeleteMapping("/v1/spaces/{spaceId}/messages/{messageId}")
    fun deleteById(
        @PathVariable spaceId: String,
        @PathVariable messageId: String,
    ): ResponseEntity<Boolean> {
        messageService.deleteById(spaceId, messageId)
        return ResponseEntity.ok(true)
    }

}
