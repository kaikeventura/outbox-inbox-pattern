package com.kaikeventura.outboxinboxpattern.inbox.consumer

import com.kaikeventura.outboxinboxpattern.common.GROUP_ID
import com.kaikeventura.outboxinboxpattern.common.TOPIC_NAME
import com.kaikeventura.outboxinboxpattern.common.toPurchaseOrder
import com.kaikeventura.outboxinboxpattern.purchase.usecase.ProcessPurchaseUseCase
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component
import java.util.*

@Component
class KafkaConsumer(
    private val processPurchaseUseCase: ProcessPurchaseUseCase
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @KafkaListener(id = "listener", topics = [TOPIC_NAME], groupId = GROUP_ID)
    fun doMessage(
        record: ConsumerRecord<String, String>,
        acknowledgment: Acknowledgment
    ) = runCatching {
        logger.info("Starting to process message: ${record.key()}")
        processPurchaseUseCase.process(
            key = UUID.fromString(record.key()),
            purchaseOrder = record.toPurchaseOrder()
        )
    }.onSuccess {
        logger.info("Success to process message: ${record.key()}")
        acknowledgment.acknowledge()
    }.onFailure {
        logger.warn("Error when trying process message: ${record.key()}", it)
    }
}
