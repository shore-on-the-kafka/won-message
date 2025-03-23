package com.won.message.user

import com.fasterxml.jackson.annotation.JsonValue
import com.won.message.controller.request.UserCreateReqeustBody
import com.won.message.id.UserIdGenerator
import jakarta.validation.constraints.NotBlank
import java.time.Instant

data class User(
    val id: UserId,
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

    fun addJoinedSpaceId(spaceId: String): User {
        return copy(joinedSpaceIds = joinedSpaceIds + spaceId)
    }
}

data class UserId(
    @JsonValue
    @field:NotBlank
    val value: String,
) {
    init {
        require(value.isNotBlank()) { "User id must not be blank" }
    }
}
