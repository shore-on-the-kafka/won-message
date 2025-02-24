package com.won.message.id

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object SpaceIdGenerator {

    fun generate(): String = generateByUuid()

    @OptIn(ExperimentalUuidApi::class)
    private fun generateByUuid(): String = Uuid.random().toString()

}
