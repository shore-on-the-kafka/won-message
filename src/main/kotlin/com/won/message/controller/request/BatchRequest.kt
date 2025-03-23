package com.won.message.controller.request

import org.springframework.http.HttpMethod

data class BatchRequest(
    val requests: List<SingleRequest>,
)

data class SingleRequest(
    val method: HttpMethod,
    val path: String,
    val body: String?,
)
