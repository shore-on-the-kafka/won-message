package com.won.message.user

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    fun create(user: User): User {
        val encryptedUser = user.encryptPassword(passwordEncoder)
        userRepository.create(encryptedUser)
        return encryptedUser
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
