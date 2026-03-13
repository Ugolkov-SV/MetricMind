package io.ugolkov.metric_mind.spring.config

import io.ugolkov.metric_mind.api.v1.apiV1Mapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter

@Suppress("unused")
@Configuration
class SerializationConfiguration {
    @Bean
    fun messageConverter(): KotlinSerializationJsonHttpMessageConverter {
        return KotlinSerializationJsonHttpMessageConverter(apiV1Mapper)
    }
}