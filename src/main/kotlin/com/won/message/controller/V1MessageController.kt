package com.won.message.controller

import com.won.message.model.Message
import org.springframework.web.bind.annotation.*

@RestController
class V1MessageController {

    @PostMapping("/v1/spaces/{spaceId}/message")
    fun create(spaceId: String): Message {
        TODO()
    }

    @GetMapping("/v1/spaces/{spaceId}/message/{messageId}")
    fun get(spaceId: String, messageId: String): Message {
        TODO()
    }

    @PutMapping("/v1/spaces/{spaceId}/message/{messageId}")
    fun updateById(spaceId: String, messageId: String): Message {
        TODO()
    }

    @DeleteMapping("/v1/spaces/{spaceId}/message/{messageId}")
    fun deleteById(spaceId: String, messageId: String): Boolean {
        TODO()
    }

}
