package com.kaikeventura.outboxinboxpattern.outbox.producer

import com.kaikeventura.outboxinboxpattern.common.KafkaPayload
import com.kaikeventura.outboxinboxpattern.common.Mapper.Companion.writeJsonString
import com.kaikeventura.outboxinboxpattern.common.TOPIC_NAME
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>
) {

    fun <T> send(key: String, payload: T) {
        kafkaTemplate.send(
            TOPIC_NAME,
            key,
            KafkaPayload(
                resource = payload!!::class.java.name,
                payload = payload
            ).writeJsonString()
        )
    }
}
