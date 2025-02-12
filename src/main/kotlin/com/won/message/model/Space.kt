package com.won.message.model

import java.time.Instant

data class Space(
    val id: String,
    val displayName: String,
    val spaceType: String, // Change type to SpaceType
    val permissionSettings: String, // Change type to PermissionSettings
    val createTime: Instant,
)
