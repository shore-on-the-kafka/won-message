package com.won.message

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import com.won.message.message.Message

object TestObjectFactory {
    val fixtureMonkey = FixtureMonkey.builder().plugin(KotlinPlugin()).build()

    fun createSpaceId(): String = fixtureMonkey.giveMeOne()

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
