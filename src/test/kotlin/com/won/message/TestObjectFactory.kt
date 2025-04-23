package com.won.message

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import com.won.message.id.MessageIdGenerator
import com.won.message.id.SpaceIdGenerator
import com.won.message.id.UserIdGenerator
import com.won.message.message.Message
import com.won.message.space.Space
import com.won.message.user.User
import com.won.message.user.UserId

object TestObjectFactory {
    val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .plugin(JakartaValidationPlugin())
        .build()

    fun createUserId(): UserId = UserIdGenerator.generate()
    fun createUser(userId: UserId = createUserId()) = fixtureMonkey.giveMeKotlinBuilder<User>()
        .set(User::id, userId)
        .build().sample()

    fun createSpaceId(): String = SpaceIdGenerator.generate()
    fun createSpace(spaceId: String = createSpaceId()) = fixtureMonkey.giveMeKotlinBuilder<Space>()
        .set(Space::id, spaceId)
        .build().sample()

    fun getSpaceFixtureBuilder() = fixtureMonkey.giveMeKotlinBuilder<Space>()

    fun createMessageId(): String = MessageIdGenerator.generate()
    fun createMessage(
        spaceId: String = createSpaceId(),
        messageId: String = createMessageId(),
    ) = fixtureMonkey.giveMeKotlinBuilder<Message>()
        .set(Message::spaceId, spaceId)
        .set(Message::id, messageId)
        .build().sample()

    fun getMessageFixtureBuilder() = fixtureMonkey.giveMeKotlinBuilder<Message>()

}
