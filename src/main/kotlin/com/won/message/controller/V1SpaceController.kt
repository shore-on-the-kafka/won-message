package com.won.message.controller

import com.won.message.controller.request.SpaceCreateReqeustBody
import com.won.message.exception.SpaceException
import com.won.message.space.Space
import com.won.message.space.SpaceService
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
class V1SpaceController(
    private val spaceService: SpaceService,
) {

    @PostMapping("/v1/spaces")
    fun create(@RequestBody body: SpaceCreateReqeustBody): Space {
        val space = Space.create(body, Instant.now())
        return spaceService.create(space)
    }

    @GetMapping("/v1/spaces/{spaceId}")
    fun get(@PathVariable spaceId: String): Space {
        return kotlin.runCatching { spaceService.getOrException(spaceId) }
            .getOrElse {
                when (it) {
                    is IllegalArgumentException -> throw SpaceException.SpaceNotFoundException()
                    else -> throw it
                }
            }
    }

    @DeleteMapping("/v1/spaces/{spaceId}")
    fun deleteById(@PathVariable spaceId: String): Boolean {
        spaceService.deleteById(spaceId)
        return true
    }

}
