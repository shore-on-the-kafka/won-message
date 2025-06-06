package com.won.message

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.won.message.controller.request.MessageCreateReqeustBody
import com.won.message.message.Message
import com.won.message.security.Roles
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class MessageIntegrationTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
) {

    @Test
    @WithMockUser(roles = [Roles.USER])
    fun `create and get message`() {
        val spaceId = TestObjectFactory.createSpaceId()
        val createRequestBody = TestObjectFactory.fixtureMonkey.giveMeOne(MessageCreateReqeustBody::class.java)

        val createResponse = mockMvc.perform(
            post("/v1/spaces/$spaceId/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequestBody))
        ).andReturn().response
        val createdMessage = objectMapper.readValue<Message>(createResponse.contentAsString)

        val getResponse = mockMvc.perform(
            get("/v1/spaces/$spaceId/messages/${createdMessage.id}")
        ).andReturn().response
        val gottenMessage = objectMapper.readValue<Message>(getResponse.contentAsString)

        assertEquals(200, createResponse.status)
        assertEquals(200, getResponse.status)
        assertEquals(createdMessage, gottenMessage)
    }

    @Test
    @WithMockUser(roles = [Roles.USER])
    fun `create and get messages in one space`() {
        val spaceId = TestObjectFactory.createSpaceId()
        val createRequestBody1 = TestObjectFactory.fixtureMonkey.giveMeOne(MessageCreateReqeustBody::class.java)
        val createRequestBody2 = TestObjectFactory.fixtureMonkey.giveMeOne(MessageCreateReqeustBody::class.java)
        val createRequestBody3 = TestObjectFactory.fixtureMonkey.giveMeOne(MessageCreateReqeustBody::class.java)
        listOf(createRequestBody1, createRequestBody2, createRequestBody3).forEach {
            mockMvc.perform(
                post("/v1/spaces/$spaceId/messages")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(it))
            )
        }

        val getListResponse = mockMvc.perform(
            get("/v1/spaces/$spaceId/messages")
        ).andReturn().response
        val messageList = objectMapper.readValue<List<Message>>(getListResponse.contentAsString)

        assertEquals(200, getListResponse.status)
        assertEquals(3, messageList.size)
        val expectedMessageList = listOf(createRequestBody3, createRequestBody2, createRequestBody1)
        messageList.forEachIndexed { index, message ->
            assertEquals(spaceId, message.spaceId)
            assertEquals(expectedMessageList[index].content, message.content)
            assertEquals(expectedMessageList[index].senderId, message.senderId)
            assertEquals(expectedMessageList[index].substitution, message.substitution)
        }
    }

    @Test
    @WithMockUser(roles = [Roles.USER])
    fun `create and get messages in multiple space`() {
        val spaceId1 = TestObjectFactory.createSpaceId()
        val spaceId2 = TestObjectFactory.createSpaceId()
        val spaceId3 = TestObjectFactory.createSpaceId()
        val createRequestBody1 = TestObjectFactory.fixtureMonkey.giveMeOne(MessageCreateReqeustBody::class.java)
        val createRequestBody2 = TestObjectFactory.fixtureMonkey.giveMeOne(MessageCreateReqeustBody::class.java)
        val createRequestBody3 = TestObjectFactory.fixtureMonkey.giveMeOne(MessageCreateReqeustBody::class.java)

        listOf(
            Pair(spaceId1, createRequestBody1),
            Pair(spaceId2, createRequestBody2),
            Pair(spaceId3, createRequestBody3)
        ).forEach {
            mockMvc.perform(
                post("/v1/spaces/${it.first}/messages")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(it.second))
            )
        }

        val getListResponse = mockMvc.perform(
            post("/v1/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listOf(spaceId1, spaceId2, spaceId3)))
        ).andReturn().response
        val messageList = objectMapper.readValue<List<Message>>(getListResponse.contentAsString)

        assertEquals(200, getListResponse.status)
        assertEquals(3, messageList.size)
        val expectedSpaceMessagePairList = listOf(
            Pair(spaceId3, createRequestBody3),
            Pair(spaceId2, createRequestBody2),
            Pair(spaceId1, createRequestBody1)
        )
        messageList.forEachIndexed { index, message ->
            assertEquals(expectedSpaceMessagePairList[index].first, message.spaceId)
            assertEquals(expectedSpaceMessagePairList[index].second.content, message.content)
            assertEquals(expectedSpaceMessagePairList[index].second.senderId, message.senderId)
            assertEquals(expectedSpaceMessagePairList[index].second.substitution, message.substitution)
        }
    }

    @Test
    @WithMockUser(roles = [Roles.USER])
    fun `delete message`() {
        val spaceId = TestObjectFactory.createSpaceId()
        val createRequestBody = TestObjectFactory.fixtureMonkey.giveMeOne(MessageCreateReqeustBody::class.java)

        val createResponse = mockMvc.perform(
            post("/v1/spaces/$spaceId/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequestBody))
        ).andReturn().response
        val createdMessage = objectMapper.readValue<Message>(createResponse.contentAsString)

        val deleteResponse = mockMvc.perform(
            delete("/v1/spaces/$spaceId/messages/${createdMessage.id}")
        ).andReturn().response

        val getResponse = mockMvc.perform(
            get("/v1/spaces/$spaceId/messages/${createdMessage.id}")
        ).andReturn().response

        assertEquals(200, deleteResponse.status)
        assertEquals(404, getResponse.status)
    }

}
