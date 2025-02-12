package com.won.message.model

import java.time.Instant

data class Message(
    val id: String,
    val spaceId: String,
    val senderId: String,
    val content: String,
    val substitution: String, // Change type to Substitution
    val createTime: Instant,
)
