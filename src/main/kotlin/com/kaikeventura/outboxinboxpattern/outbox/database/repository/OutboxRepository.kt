package com.kaikeventura.outboxinboxpattern.outbox.database.repository

import com.kaikeventura.outboxinboxpattern.outbox.database.entity.OutboxEntity
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OutboxRepository : JpaRepository<OutboxEntity, UUID>
