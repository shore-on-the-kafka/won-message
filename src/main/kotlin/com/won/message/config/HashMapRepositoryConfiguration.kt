package com.won.message.config

import com.won.message.infrastructure.HashMapRepository
import com.won.message.message.Message
import com.won.message.message.MessageRepository
import com.won.message.space.Space
import com.won.message.space.SpaceRepository
import com.won.message.user.User
import com.won.message.user.UserId
import com.won.message.user.UserRepository
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

    @Repository
    class SpaceHashMapRepository : HashMapRepository<Space, String>(), SpaceRepository {
        override fun getKey(entity: Space) = entity.id
    }

    @Repository
    class UserHashMapRepository : HashMapRepository<User, UserId>(), UserRepository {
        override fun getByName(name: String): User? = map.values.firstOrNull { it.name == name }
        override fun getKey(entity: User) = entity.id
    }

}
