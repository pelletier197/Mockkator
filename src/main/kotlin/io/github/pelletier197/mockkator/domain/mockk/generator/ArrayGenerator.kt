package io.github.pelletier197.mockkator.domain.mockk.generator

import com.intellij.psi.PsiArrayType
import com.intellij.psi.PsiPrimitiveType
import com.intellij.psi.PsiType
import io.github.pelletier197.mockkator.domain.mockk.UnderTestParameterInjector
import io.github.pelletier197.mockkator.domain.mockk.generator.Utils.extractType

object ArrayGenerator {
    fun generateArray(context: UnderTestParameterInstantiationContext) {
        val type = extractType(context.currentElement)!!
        val arrayElementName = CollectionGenerator.generateCollectionElementName(context.parameterName)
        context.createParameterWithInstantiationDeclaration("${selectKotlinArrayGeneratorFunction(type)}($arrayElementName)")
        UnderTestParameterInjector.createUnderTestParameter(
            context = context.createForSubVariable(
                parameterName = arrayElementName,
                element = type,
            )
        )
    }

    private fun selectKotlinArrayGeneratorFunction(type: PsiType): String {
        if (type !is PsiArrayType) return "Object"
        return when (val componentType = type.componentType) {
            is PsiPrimitiveType -> when (componentType.name) {
                "int" -> "intArrayOf"
                "float" -> "floatArrayOf"
                "double" -> "doubleArrayOf"
                "long" -> "longArrayOf"
                "char" -> "charArrayOf"
                "short" -> "shortArrayOf"
                "boolean" -> "booleanArrayOf"
                "byte" -> "byteArrayOf"
                else -> "arrayOf"
            }
            else -> "arrayOf"
        }
    }
}
