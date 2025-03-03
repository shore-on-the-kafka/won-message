package com.won.message

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import com.won.message.message.Message
import com.won.message.space.Space
import com.won.message.user.User
import com.won.message.user.UserId

object TestObjectFactory {
    val fixtureMonkey = FixtureMonkey.builder().plugin(KotlinPlugin()).build()

    fun createUserId(): UserId = fixtureMonkey.giveMeOne()
    fun createUser(userId: UserId = createUserId()) = fixtureMonkey.giveMeKotlinBuilder<User>()
        .set(User::id, userId)
        .build().sample()

    fun createSpaceId(): String = fixtureMonkey.giveMeOne()
    fun createSpace(spaceId: String = createSpaceId()) = fixtureMonkey.giveMeKotlinBuilder<Space>()
        .set(Space::id, spaceId)
        .build().sample()

    fun getSpaceFixtureBuilder() = fixtureMonkey.giveMeKotlinBuilder<Space>()

    fun createMessageId(): String = fixtureMonkey.giveMeOne()
    fun createMessage(
        spaceId: String = createSpaceId(),
        messageId: String = createMessageId(),
    ) = fixtureMonkey.giveMeKotlinBuilder<Message>()
        .set(Message::spaceId, spaceId)
        .set(Message::id, messageId)
        .build().sample()

    fun getMessageFixtureBuilder() = fixtureMonkey.giveMeKotlinBuilder<Message>()

}
