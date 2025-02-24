package com.won.message.controller.request

import com.won.message.space.SpaceType

data class SpaceCreateReqeustBody(
    val displayName: String,
    val spaceType: SpaceType,
    val permissionSettings: String,
)
