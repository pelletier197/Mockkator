package io.github.pelletier197.mockkator.domain.mockk.generator

import kotlin.math.truncate
import kotlin.random.Random

object LibraryGenerator {
    fun generateUUID(context: UnderTestParameterInstantiationContext) {
        context.createParameterWithInstantiationDeclaration("java.util.UUID.randomUUID()")
    }

    fun generateBigDecimal(context: UnderTestParameterInstantiationContext) {
        context.createParameterWithInstantiationDeclaration("java.math.BigDecimal.valueOf(${truncate(Random.nextDouble(50_000.0, 500_000.0))})")
    }

    fun generateBigInteger(context: UnderTestParameterInstantiationContext) {
        context.createParameterWithInstantiationDeclaration("java.math.BigInteger.valueOf(${Random.nextInt(50_000, 500_000)})")
    }
}
