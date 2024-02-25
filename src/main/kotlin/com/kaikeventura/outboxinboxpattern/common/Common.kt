package com.kaikeventura.outboxinboxpattern.common

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule

const val TOPIC_NAME = "orders"

open class Mapper {
    companion object {

        private val mapper = ObjectMapper().registerModules(JavaTimeModule())

        fun <T> T.writeJsonString(): String = mapper.writeValueAsString(this)
        fun <T> String.buildClass(source: Class<T>): T = mapper.readValue(this, source)
    }
}
