package com.won.message

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.won.message.controller.request.UserCreateReqeustBody
import com.won.message.security.Roles
import com.won.message.user.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserIntegrationTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
) {

    @Test
    @WithMockUser(roles = [Roles.USER])
    fun `create and get user`() {
        val createRequestBody = TestObjectFactory.fixtureMonkey.giveMeOne(UserCreateReqeustBody::class.java)

        val createResponse = mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequestBody))
        ).andReturn().response
        val createdUser = objectMapper.readValue<User>(createResponse.contentAsString)

        val getResponse = mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/${createdUser.id.value}"))
            .andReturn().response
        val gottenUser = objectMapper.readValue<User>(getResponse.contentAsString)

        assertEquals(200, createResponse.status)
        assertEquals(200, getResponse.status)

        assertEquals(createdUser.id, gottenUser.id)
        assertEquals(createdUser.name, gottenUser.name)
        assertEquals(createdUser.joinedSpaceIds, gottenUser.joinedSpaceIds)
        assertEquals(createdUser.createTime, gottenUser.createTime)

        assert(gottenUser.password.startsWith("{bcrypt}")) { "Password is not properly encoded" }
    }

}
