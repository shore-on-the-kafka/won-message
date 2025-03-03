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

}
