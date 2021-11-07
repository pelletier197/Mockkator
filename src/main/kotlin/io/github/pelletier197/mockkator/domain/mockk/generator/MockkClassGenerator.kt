package io.github.pelletier197.mockkator.domain.mockk.generator

import io.github.pelletier197.mockkator.domain.mockk.generator.Utils.extractType

object MockkClassGenerator {
    fun generateMockk(context: UnderTestParameterInstantiationContext) {
        val type = extractType(context.currentElement)!!
        context.createParameterWithInstantiationDeclaration("io.mockk.mockk<${replaceJavaTypesWithKotlin(type.canonicalText)}>()")
    }

    private fun replaceJavaTypesWithKotlin(input: String) : String {
        return input
            .replace("java.lang.String", "String")
            .replace("java.lang.Integer", "Int")
            .replace("java.lang.Boolean", "Boolean")
            .replace("java.lang.Long", "Long")
            .replace("java.lang.Float", "Float")
            .replace("java.lang.Double", "Double")
            .replace("java.lang.Byte", "Byte")
            .replace("java.lang.Character", "Char")
    }
}
