package com.kaikeventura.outboxinboxpattern.inbox.database.repository

import com.kaikeventura.outboxinboxpattern.inbox.database.entity.InboxEntity
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InboxRepository : JpaRepository<InboxEntity, UUID>
