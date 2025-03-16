package com.won.message.space

import com.won.message.user.UserService
import org.springframework.stereotype.Service

@Service
class SpaceService(
    private val userService: UserService,
    private val spaceRepository: SpaceRepository,
) {
    fun create(entity: Space): Space {
        val createdSpace = spaceRepository.create(entity)
        createdSpace.getJoinedUserIds().forEach { userId ->
            kotlin.runCatching { userService.addJoinedSpaceId(userId, createdSpace.id) }
        }
        return createdSpace
    }

    fun getOrException(id: String): Space {
        return requireNotNull(spaceRepository.get(id)) { "Space not found" }
    }

    fun update(entity: Space): Space {
        return spaceRepository.update(entity)
    }

    fun deleteById(id: String) {
        spaceRepository.deleteById(id)
    }
}
