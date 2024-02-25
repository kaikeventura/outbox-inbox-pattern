package com.kaikeventura.outboxinboxpattern.purchase.database.repository

import com.kaikeventura.outboxinboxpattern.purchase.database.entity.PurchaseOrderEntity
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository

interface PurchaseOrderRepository : JpaRepository<PurchaseOrderEntity, UUID>
