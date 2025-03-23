package com.won.message.id

import com.won.message.user.UserId
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object UserIdGenerator {

    fun generate(): UserId = generateByUuid()

    @OptIn(ExperimentalUuidApi::class)
    private fun generateByUuid(): UserId = UserId(value = Uuid.random().toString())

}
