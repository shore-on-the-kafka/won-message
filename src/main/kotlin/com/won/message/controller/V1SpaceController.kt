package com.won.message.controller

import com.won.message.controller.request.SpaceCreateReqeustBody
import com.won.message.space.Space
import com.won.message.space.SpaceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
class V1SpaceController(
    private val spaceService: SpaceService,
) {

    @PostMapping("/v1/spaces")
    fun create(@RequestBody body: SpaceCreateReqeustBody): ResponseEntity<Space> {
        val space = Space.create(body, Instant.now())
        return ResponseEntity.ok(spaceService.create(space))
    }

    @GetMapping("/v1/spaces/{spaceId}")
    fun get(@PathVariable spaceId: String): ResponseEntity<Space> {
        return kotlin.runCatching { ResponseEntity.ok(spaceService.getOrException(spaceId)) }
            .getOrElse {
                when (it) {
                    is IllegalArgumentException -> ResponseEntity.notFound().build()
                    else -> throw it
                }
            }
    }

    @DeleteMapping("/v1/spaces/{spaceId}")
    fun deleteById(@PathVariable spaceId: String): ResponseEntity<Boolean> {
        spaceService.deleteById(spaceId)
        return ResponseEntity.ok(true)
    }

}
