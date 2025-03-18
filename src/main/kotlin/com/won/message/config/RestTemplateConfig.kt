package com.won.message.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.DefaultUriBuilderFactory

@Configuration
class RestTemplateConfig {

    @Bean
    fun restTemplate(): RestTemplate {
        //TODO Change base url to using spring properties
        val restTemplate = RestTemplate()
        restTemplate.uriTemplateHandler = DefaultUriBuilderFactory("http://localhost:8080");
        return restTemplate
    }

}
