package io.github.pelletier197.mockkator.domain.mockk.generator

import kotlin.random.Random

object PrimitiveGenerator {
    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    private val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()

    fun generateBoolean(context: UnderTestParameterInstantiationContext) {
        context.createParameterWithInstantiationDeclaration(Random.nextBoolean().toString())
    }

    fun generateInteger(context: UnderTestParameterInstantiationContext) {
        context.createParameterWithInstantiationDeclaration(Random.nextInt(0, 5000).toString())
    }

    fun generateLong(context: UnderTestParameterInstantiationContext) {
        context.createParameterWithInstantiationDeclaration(Random.nextLong(0, 100_000).toString() + "L")
    }

    fun generateFloat(context: UnderTestParameterInstantiationContext) {
        context.createParameterWithInstantiationDeclaration(Random.nextFloat().toString() + "f")
    }

    fun generateDouble(context: UnderTestParameterInstantiationContext) {
        context.createParameterWithInstantiationDeclaration(Random.nextDouble().toString())
    }

    fun generateByte(context: UnderTestParameterInstantiationContext) {
        context.createParameterWithInstantiationDeclaration(Random.nextBytes(1)[0].toString())
    }

    fun generateChar(context: UnderTestParameterInstantiationContext) {
        context.createParameterWithInstantiationDeclaration("'${charPool[Random.nextInt(0, charPool.size)]}'")
    }

    fun generateString(context: UnderTestParameterInstantiationContext) {
        context.createParameterWithInstantiationDeclaration("\"${toSnakeCase(context.parameterName)}\"")
    }

    private fun toSnakeCase(value: String): String {
        return camelRegex.replace(value) {
            "_${it.value}"
        }.toLowerCase()
    }
}
