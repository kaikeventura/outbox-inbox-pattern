package com.kaikeventura.outboxinboxpattern.purchase.usecase

import com.kaikeventura.outboxinboxpattern.common.Mapper.Companion.buildClass
import com.kaikeventura.outboxinboxpattern.inbox.database.entity.InboxEntity
import com.kaikeventura.outboxinboxpattern.inbox.database.entity.toInboxEntity
import com.kaikeventura.outboxinboxpattern.inbox.database.repository.InboxRepository
import com.kaikeventura.outboxinboxpattern.purchase.database.entity.PurchaseOrderEntity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.util.UUID

@Component
class ProcessPurchaseUseCase(
    private val inboxRepository: InboxRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @OptIn(DelicateCoroutinesApi::class)
    fun process(key: UUID, purchaseOrder: PurchaseOrderEntity) {
        if (inboxRepository.existsById(key)) {
            logger.info("Purchase: $key already processed")
            return
        }
        val inbox = purchaseOrder.toInboxEntity().save()

        GlobalScope.launch {
            doProcess(
                purchaseOrder = purchaseOrder,
                inbox = inbox
            )
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun reprocess(key: UUID) {
        val inbox = inboxRepository.findByIdOrNull(key)
            ?: throw RuntimeException("Inbox not found: $key")
        val clazz = Class.forName(inbox.source) as PurchaseOrderEntity

        GlobalScope.launch {
            doProcess(
                purchaseOrder = inbox.payload.buildClass(clazz::class.java),
                inbox = inbox
            )
        }
    }

    private suspend fun doProcess(purchaseOrder: PurchaseOrderEntity, inbox: InboxEntity) {
        logger.info("Starting process purchase order: ${purchaseOrder.id}")
        logger.info("Purchase order: $purchaseOrder")
        inbox.markAsProcessed()
        logger.info("Finishing process purchase order: ${purchaseOrder.id}")
    }

    private fun InboxEntity.markAsProcessed(): InboxEntity =
        copy(
            processedAt = now()
        ).save()

    private fun InboxEntity.save(): InboxEntity =
        inboxRepository.save(this)
}
