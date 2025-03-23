package com.won.message.space

import com.won.message.controller.request.SpaceCreateReqeustBody
import com.won.message.id.SpaceIdGenerator
import com.won.message.user.UserId
import java.time.Instant

data class Space(
    val id: String,
    val displayName: String,
    val spaceType: SpaceType,
    val permissionSettings: PermissionSettings,
    val createTime: Instant,
) {
    companion object {
        fun create(reqeust: SpaceCreateReqeustBody, requestTime: Instant) = Space(
            id = SpaceIdGenerator.generate(),
            displayName = reqeust.displayName,
            spaceType = reqeust.spaceType,
            permissionSettings = reqeust.permissionSettings,
            createTime = requestTime,
        )
    }

    fun getJoinedUserIds(): List<UserId> =
        (permissionSettings.readableUserIds + permissionSettings.writableUserIds).distinct()
}

data class PermissionSettings(
    val readableUserIds: List<UserId>,
    val writableUserIds: List<UserId>,
)
