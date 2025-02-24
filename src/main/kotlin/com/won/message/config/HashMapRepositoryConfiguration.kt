package com.won.message.config

import com.won.message.infrastructure.HashMapRepository
import com.won.message.message.Message
import com.won.message.message.MessageRepository
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Repository

@Configuration
class HashMapRepositoryConfiguration {

    @Repository
    class MessageHashMapRepository : HashMapRepository<Message, String>(), MessageRepository {
        override fun getListBySpaceId(spaceId: String): List<Message> =
            map.values.filter { it.spaceId == spaceId }
                .sortedByDescending { it.createTime }

        override fun getKey(entity: Message) = entity.id
    }

}
