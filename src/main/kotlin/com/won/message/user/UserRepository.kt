package com.won.message.user

import com.won.message.infrastructure.Repository

interface UserRepository : Repository<User, UserId> {
    fun getByName(name: String): User?
}
