package com.won.message.space

import org.springframework.stereotype.Service

@Service
class SpaceService(
    private val spaceRepository: SpaceRepository,
) {
    fun create(entity: Space): Space {
        return spaceRepository.create(entity)
    }

    fun getOrException(id: String): Space {
        return requireNotNull(spaceRepository.getOrNull(id)) { "Space not found" }
    }

    fun update(entity: Space): Space {
        return spaceRepository.update(entity)
    }

    fun deleteById(id: String) {
        spaceRepository.deleteById(id)
    }
}
