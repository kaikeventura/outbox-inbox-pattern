package com.kaikeventura.outboxinboxpattern.common

import com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

const val TOPIC_NAME = "orders"

data class KafkaPayload<T>(
    val resource: String,
    val payload: T
)

open class Mapper {
    companion object {

        private val mapper = jacksonObjectMapper().apply {
            registerModule(JavaTimeModule())
            disable(WRITE_DATES_AS_TIMESTAMPS)
        }

        fun <T> T.writeJsonString(): String = mapper.writeValueAsString(this)

        fun <T> String.buildClass(clazz: Class<T>): T = mapper.readValue(this, clazz)
    }
}
