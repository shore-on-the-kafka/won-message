package com.won.message.message

import com.won.message.controller.request.MessageCreateReqeustBody
import com.won.message.id.MessageIdGenerator
import java.time.Instant

data class Message(
    val id: String,
    val spaceId: String,
    val senderId: String,
    val content: String,
    val substitution: String, // Change type to Substitution
    val createTime: Instant,
) {
    companion object {
        fun create(spaceId: String, reqeust: MessageCreateReqeustBody, requestTime: Instant) = Message(
            id = MessageIdGenerator.generate(),
            spaceId = spaceId,
            senderId = reqeust.senderId,
            content = reqeust.content,
            substitution = reqeust.substitution,
            createTime = requestTime,
        )
    }
}
