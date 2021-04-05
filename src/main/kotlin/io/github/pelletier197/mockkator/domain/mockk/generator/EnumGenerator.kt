package io.github.pelletier197.mockkator.domain.mockk.generator

import com.intellij.psi.PsiType
import com.intellij.psi.util.PsiUtil
import org.jetbrains.kotlin.asJava.classes.KtUltraLightClass

object EnumGenerator {
    fun generateEnum(context: UnderTestParameterInstantiationContext) {
        context.createParameterWithInstantiationDeclaration(generateKotlin(context))
    }

    private fun generateKotlin(context: UnderTestParameterInstantiationContext): String {
        val element = extractClass(context)
        if (element is KtUltraLightClass) {
            val enumValues = element.fields.asList().ifEmpty { return "null" }
            return "${element.qualifiedName}.${enumValues.random().name}"
        }
        return "null"
    }

    private fun extractClass(context: UnderTestParameterInstantiationContext): Any {
        val element = context.currentElement
        if (element is PsiType) return PsiUtil.resolveClassInType(element) ?: element
        return element
    }
}
