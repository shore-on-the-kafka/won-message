package com.won.message.user

import com.won.message.controller.request.UserCreateReqeustBody
import com.won.message.id.UserIdGenerator
import java.time.Instant

data class User(
    val id: String,
    val name: String,
    val joinedSpaceIds: List<String>,
    val createTime: Instant,
) {
    companion object {
        fun create(request: UserCreateReqeustBody, requestTime: Instant) = User(
            id = UserIdGenerator.generate(),
            name = request.name,
            joinedSpaceIds = emptyList(),
            createTime = requestTime,
        )
    }
}
