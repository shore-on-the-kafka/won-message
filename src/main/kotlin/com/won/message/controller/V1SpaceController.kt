package com.won.message.controller

import com.won.message.space.Space
import org.springframework.web.bind.annotation.*

@RestController
class V1SpaceController {

    @PostMapping("/v1/spaces")
    fun create(): Space {
        TODO()
    }

    @GetMapping("/v1/spaces/{spaceId}")
    fun get(spaceId: String): Space {
        TODO()
    }

    @PutMapping("/v1/spaces/{spaceId}")
    fun update(spaceId: String): Space {
        TODO()
    }

    @DeleteMapping("/v1/spaces/{spaceId}")
    fun delete(spaceId: String): Boolean {
        TODO()
    }

}
