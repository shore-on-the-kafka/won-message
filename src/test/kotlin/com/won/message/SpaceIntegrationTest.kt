package com.won.message

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.won.message.controller.request.SpaceCreateReqeustBody
import com.won.message.security.Roles
import com.won.message.space.Space
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
class SpaceIntegrationTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
) {

    @Test
    @WithMockUser(roles = [Roles.USER])
    fun `create and get space`() {
        val createRequestBody = TestObjectFactory.fixtureMonkey.giveMeOne(SpaceCreateReqeustBody::class.java)

        val createResponse = mockMvc.perform(
            post("/v1/spaces")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequestBody))
        ).andReturn().response
        val createdSpace = objectMapper.readValue<Space>(createResponse.contentAsString)

        val getResponse = mockMvc.perform(get("/v1/spaces/${createdSpace.id}")).andReturn().response
        val gottenSpace = objectMapper.readValue<Space>(getResponse.contentAsString)

        assertEquals(200, createResponse.status)
        assertEquals(200, getResponse.status)
        assertEquals(createdSpace, gottenSpace)
    }

    @Test
    @WithMockUser(roles = [Roles.USER])
    fun `delete space`() {
        val createRequestBody = TestObjectFactory.fixtureMonkey.giveMeOne(SpaceCreateReqeustBody::class.java)

        val createResponse = mockMvc.perform(
            post("/v1/spaces")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequestBody))
        ).andReturn().response
        val createdSpace = objectMapper.readValue<Space>(createResponse.contentAsString)

        val deleteResponse = mockMvc.perform(delete("/v1/spaces/${createdSpace.id}")).andReturn().response

        val getResponse = mockMvc.perform(get("/v1/spaces/${createdSpace.id}")).andReturn().response

        assertEquals(200, deleteResponse.status)
        assertEquals(404, getResponse.status)
    }

}
