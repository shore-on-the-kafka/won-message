package com.won.message.controller.request

data class BatchResponse(
    val responses: List<SingleResponse>,
)

data class SingleResponse(
    val status: Int,
    val body: String?,
)
