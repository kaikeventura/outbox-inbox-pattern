package com.kaikeventura.outboxinboxpattern.common

import com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kaikeventura.outboxinboxpattern.common.Mapper.Companion.buildClass
import com.kaikeventura.outboxinboxpattern.purchase.database.entity.PurchaseOrderEntity
import org.apache.kafka.clients.consumer.ConsumerRecord

const val TOPIC_NAME = "orders"
const val GROUP_ID = "orders-consumer"

data class KafkaPayload<T>(
    val resource: String,
    val payload: T
)

fun ConsumerRecord<String, String>.toPurchaseOrder(): PurchaseOrderEntity {
    val kafkaPayload = this.value().buildClass(KafkaPayload::class.java)
    return kafkaPayload.payload as PurchaseOrderEntity
}

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
