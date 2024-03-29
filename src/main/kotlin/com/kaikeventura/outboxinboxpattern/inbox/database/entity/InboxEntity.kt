package com.kaikeventura.outboxinboxpattern.inbox.database.entity

import com.kaikeventura.outboxinboxpattern.common.Mapper.Companion.writeJsonString
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.util.UUID
import org.hibernate.annotations.CreationTimestamp

@Entity(name = "inbox")
data class InboxEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    val source: String,

    @Column(length = Int.MAX_VALUE)
    val payload: String,

    @CreationTimestamp
    val createdAt: LocalDateTime? = null,

    val processedAt: LocalDateTime? = null
)

fun <T> T.toInboxEntity(processedAt: LocalDateTime? = null): InboxEntity =
    InboxEntity(
        source = this!!::class.toString(),
        payload = writeJsonString(),
        processedAt = processedAt
    )
