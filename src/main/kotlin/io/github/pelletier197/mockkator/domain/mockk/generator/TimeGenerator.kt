package io.github.pelletier197.mockkator.domain.mockk.generator

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Period
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import kotlin.random.Random

object TimeGenerator {
    fun generateInstant(context: UnderTestParameterInstantiationContext) {
        return context.createParameterWithInstantiationDeclaration("java.time.Instant.parse(\"${generateRandomInstant()}\")")
    }

    fun generateZoneDateTime(context: UnderTestParameterInstantiationContext) {
        return context.createParameterWithInstantiationDeclaration(
            "java.time.ZonedDateTime.parse(\"${
            generateRandomInstant().atZone(
                generateRandomZoneId()
            )
            }\")"
        )
    }

    fun generateZoneId(context: UnderTestParameterInstantiationContext) {
        return context.createParameterWithInstantiationDeclaration("java.time.ZoneId.of(\"${generateRandomZoneId().id}\")")
    }

    fun generateLocalDate(context: UnderTestParameterInstantiationContext) {
        val date = LocalDate.ofInstant(generateRandomInstant(), generateRandomZoneId())
        return context.createParameterWithInstantiationDeclaration("java.time.LocalDate.parse(\"$date\")")
    }

    fun generateLocalDateTime(context: UnderTestParameterInstantiationContext) {
        val dateTime = LocalDateTime.ofInstant(generateRandomInstant(), generateRandomZoneId())
        return context.createParameterWithInstantiationDeclaration("java.time.LocalDateTime.parse(\"$dateTime\")")
    }

    fun generateLocalTime(context: UnderTestParameterInstantiationContext) {
        val dateTime = LocalTime.ofInstant(generateRandomInstant(), generateRandomZoneId())
        return context.createParameterWithInstantiationDeclaration("java.time.LocalTime.parse(\"$dateTime\")")
    }

    fun generatePeriod(context: UnderTestParameterInstantiationContext) {
        val first = LocalDate.ofInstant(generateRandomInstant(), generateRandomZoneId())
        val second = LocalDate.ofInstant(generateRandomInstant(), generateRandomZoneId())
        val period = Period.between(minOf(first, second), maxOf(first, second))
        return context.createParameterWithInstantiationDeclaration("java.time.Period.parse(\"$period\")")
    }

    private fun generateRandomZoneId(): ZoneId {
        val available = ZoneId.getAvailableZoneIds()
        return if (available.isEmpty()) ZoneId.systemDefault() else ZoneId.of(available.random())
    }

    private fun generateRandomInstant(): Instant {
        return Instant.ofEpochMilli(
            Random.nextLong(
                from = Instant.now().minus(365, ChronoUnit.DAYS).toEpochMilli(),
                until = Instant.now().toEpochMilli()
            )
        )
    }
}
