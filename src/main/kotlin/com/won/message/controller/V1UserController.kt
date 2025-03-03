package com.won.message.controller

import com.won.message.controller.request.UserCreateReqeustBody
import com.won.message.user.User
import com.won.message.user.UserId
import com.won.message.user.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
class V1UserController(
    private val userService: UserService,
) {

    @PostMapping("/v1/users")
    fun create(@RequestBody body: UserCreateReqeustBody): ResponseEntity<User> {
        val space = User.create(body, Instant.now())
        return ResponseEntity.ok(userService.create(space))
    }

    @GetMapping("/v1/users/{userId}")
    fun get(@PathVariable userId: UserId): ResponseEntity<User> {
        return kotlin.runCatching { ResponseEntity.ok(userService.getOrException(userId)) }
            .getOrElse {
                when (it) {
                    is IllegalArgumentException -> ResponseEntity.notFound().build()
                    else -> throw it
                }
            }
    }

}
