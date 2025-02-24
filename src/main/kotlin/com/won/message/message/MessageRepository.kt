package com.won.message.message

import com.won.message.infrastructure.Repository

interface MessageRepository : Repository<Message, String> {
    fun getListBySpaceId(spaceId: String): List<Message>
}
