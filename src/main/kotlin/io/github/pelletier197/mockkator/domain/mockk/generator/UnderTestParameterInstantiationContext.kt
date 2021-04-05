package io.github.pelletier197.mockkator.domain.mockk.generator

import com.intellij.psi.PsiParameter
import com.intellij.psi.PsiType
import io.github.pelletier197.mockkator.domain.mockk.MockkInjectionContext

data class UnderTestParameterInstantiationContext(
    val mockkContext: MockkInjectionContext,
    val originalParameter: PsiParameter,
    val currentElement: Any,
    val parameterName: String = originalParameter.name ?: "parameter",
) {
    fun createForSubVariable(parameterName: String, element: PsiType): UnderTestParameterInstantiationContext {
        return this.copy(parameterName = parameterName, currentElement = element)
    }

    fun addImportIfNotExist(import: String) {
        mockkContext.addImportIfNotExist(import)
    }

    fun createParameterWithInstantiationDeclaration(declarationText: String) {
        mockkContext.insertUnderTestParameter("val $parameterName = $declarationText")
    }
}
