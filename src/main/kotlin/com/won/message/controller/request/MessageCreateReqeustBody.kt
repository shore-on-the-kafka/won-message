package com.won.message.controller.request

data class MessageCreateReqeustBody(
    val senderId: String,
    val content: String,
    val substitution: String,
)
