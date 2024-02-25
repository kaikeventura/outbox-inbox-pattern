package com.kaikeventura.outboxinboxpattern

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OutboxInboxPatternApplication

fun main(args: Array<String>) {
	runApplication<OutboxInboxPatternApplication>(*args)
}
