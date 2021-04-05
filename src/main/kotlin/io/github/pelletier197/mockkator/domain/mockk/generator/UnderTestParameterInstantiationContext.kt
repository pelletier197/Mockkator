package io.github.pelletier197.mockkator.domain.mockk.generator

import com.intellij.psi.PsiElement
import io.github.pelletier197.mockkator.domain.mockk.MockkInjectionContext
import org.jetbrains.kotlin.psi.KtParameter

data class UnderTestParameterInstantiationContext(
    val mockkContext: MockkInjectionContext,
    val originalParameter: KtParameter,
    val currentElement: PsiElement,
    val parameterName: String = originalParameter.name ?: "parameter",
) {
    fun createForSubVariable(parameterName: String): UnderTestParameterInstantiationContext {
        return this.copy(parameterName = parameterName)
    }

    fun addImportIfNotExist(import: String) {
        mockkContext.addImportIfNotExist(import)
    }

    fun createParameterWithInstantiationDeclaration(declarationText: String) {
        mockkContext.insertUnderTestParameter("val $parameterName = $declarationText")
    }
}
