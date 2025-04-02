package com.won.message.user

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsService(
    private val userRepository: UserRepository,
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.getByName(username) ?: throw UsernameNotFoundException("User not found")

        return org.springframework.security.core.userdetails.User(
            user.name,
            user.password,
            listOf(SimpleGrantedAuthority("ROLE_USER")) // TODO: Add roles on User entity
        )
    }

}
