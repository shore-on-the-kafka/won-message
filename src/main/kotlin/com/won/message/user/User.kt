package com.won.message.user

import com.fasterxml.jackson.annotation.JsonValue
import com.won.message.controller.request.UserCreateReqeustBody
import com.won.message.id.UserIdGenerator
import jakarta.validation.constraints.NotBlank
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class User(
    val id: UserId,
    val name: String,
    val password: String,
    val joinedSpaceIds: List<String>,
    val createTime: Instant,
) {
    companion object {
        fun create(request: UserCreateReqeustBody, requestTime: Instant) = User(
            id = UserIdGenerator.generate(),
            name = request.name,
            password = request.password,
            joinedSpaceIds = emptyList(),
            createTime = requestTime,
        )

        fun createOAuth2LoginUser(name: String, requestTime: Instant) = User(
            id = UserIdGenerator.generate(),
            name = name,
            password = generateDummyPassword(),
            joinedSpaceIds = emptyList(),
            createTime = requestTime,
        )

        @OptIn(ExperimentalUuidApi::class)
        private fun generateDummyPassword(): String = Uuid.random().toString()
    }

    fun encryptPassword(passwordEncoder: PasswordEncoder): User {
        val encodedPassword = passwordEncoder.encode(password)
        return copy(password = encodedPassword)
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
