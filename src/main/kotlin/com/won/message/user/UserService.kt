package com.won.message.user

import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
) {

    fun create(user: User): User {
        return userRepository.create(user)
    }

    fun getOrException(id: UserId): User {
        return requireNotNull(userRepository.get(id)) { "User not found" }
    }

    /**
     * This method is used to update the user's information.
     * @throws IllegalArgumentException if the user is not found.
     */
    fun addJoinedSpaceId(id: UserId, spaceId: String): User {
        getOrException(id).addJoinedSpaceId(spaceId).let {
            return userRepository.update(it)
        }
    }
}
