package com.won.message.controller

import com.won.message.controller.request.BatchRequest
import com.won.message.controller.request.BatchResponse
import com.won.message.controller.request.SingleRequest
import com.won.message.controller.request.SingleResponse
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod.*
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
class V1BatchController(
    private val restTemplate: RestTemplate,
) {

    @PostMapping("/v1/batch")
    fun handleBatchRequest(@RequestBody batchRequest: BatchRequest): ResponseEntity<BatchResponse> {
        //TODO Change parallelStream to coroutines
        val responses = batchRequest.requests.parallelStream()
            .map { request -> processRequest(request) }
            .toList()
        return ResponseEntity.ok(BatchResponse(responses))
    }

    private fun processRequest(request: SingleRequest): SingleResponse {
        //TODO Change responseType(String) to the appropriate type
        return try {
            val response = when (request.method) {
                GET -> restTemplate.exchange(request.path, GET, null, String::class.java)

                POST -> restTemplate.exchange(
                    request.path, POST, HttpEntity(request.body, getJsonHeaders()), String::class.java
                )

                DELETE -> restTemplate.exchange(request.path, DELETE, null, String::class.java)

                else -> throw IllegalArgumentException("Unsupported HTTP method: ${request.method}")
            }
            SingleResponse(response.statusCode.value(), response.body)
        } catch (e: Exception) {
            SingleResponse(500, "Error: ${e.message}")
        }
    }

    private fun getJsonHeaders(): HttpHeaders {
        return HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
    }

}
