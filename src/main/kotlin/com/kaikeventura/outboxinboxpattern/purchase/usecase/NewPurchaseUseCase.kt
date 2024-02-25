package com.kaikeventura.outboxinboxpattern.purchase.usecase

import com.kaikeventura.outboxinboxpattern.outbox.database.entity.OutboxEntity
import com.kaikeventura.outboxinboxpattern.outbox.database.entity.toOutboxEntity
import com.kaikeventura.outboxinboxpattern.outbox.database.repository.OutboxRepository
import com.kaikeventura.outboxinboxpattern.outbox.producer.KafkaProducer
import com.kaikeventura.outboxinboxpattern.purchase.database.entity.PurchaseOrderEntity
import com.kaikeventura.outboxinboxpattern.purchase.database.repository.PurchaseOrderRepository
import jakarta.annotation.PostConstruct
import jakarta.transaction.Transactional
import java.time.LocalDateTime.now
import org.springframework.stereotype.Component

@Component
class Run(
    private val newPurchaseUseCase: NewPurchaseUseCase
) {
    @PostConstruct
    fun init() {
        newPurchaseUseCase.createNewPurchaseOrder()
    }
}

@Component
class NewPurchaseUseCase(
    private val purchaseOrderRepository: PurchaseOrderRepository,
    private val outboxRepository: OutboxRepository,
    private val kafkaProducer: KafkaProducer
) {

    fun createNewPurchaseOrder() {
        val purchaseOrder = buildPurchaseOrder()

        kafkaProducer.send(
            key = purchaseOrder.first.id!!.toString(),
            payload = purchaseOrder.first
        )

        purchaseOrder.second.markAsProcessed()
    }

    @Transactional(rollbackOn = [Exception::class])
    fun buildPurchaseOrder(): Pair<PurchaseOrderEntity, OutboxEntity> {
        val purchaseOrder = PurchaseOrderEntity(
            productName = "Massinha de modelar Verde",
            quantity = 10,
            price = 500 * 10,
            customerName = "Jamantinha"
        ).save()

        val outbox = purchaseOrder.toOutboxEntity().save()

        return Pair(purchaseOrder, outbox)
    }

    @Transactional(rollbackOn = [Exception::class])
    fun OutboxEntity.markAsProcessed(): OutboxEntity =
        copy(
            processedAt = now()
        ).save()

    private fun PurchaseOrderEntity.save(): PurchaseOrderEntity =
        purchaseOrderRepository.save(this)

    private fun OutboxEntity.save(): OutboxEntity =
        outboxRepository.save(this)
}
