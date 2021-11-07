package io.github.pelletier197.mockkator.domain.mockk

import com.intellij.psi.PsiParameter
import io.github.pelletier197.mockkator.domain.mockk.exception.MockkCodeInjectionException
import io.github.pelletier197.mockkator.domain.mockk.generator.UnderTestParameterInstantiationContext
import org.jetbrains.kotlin.psi.KtProperty

class NoPrimaryConstructorDefinedException :
    MockkCodeInjectionException("No primary constructor was found for your class")

class MockkCodeInjector {
    private val underTestVariableName = "underTest"

    fun inject(context: MockkInjectionContext) {
        val targetConstructor = context.testedClass.primaryConstructor ?: throw NoPrimaryConstructorDefinedException()
        val parameters = targetConstructor.parameterList.parameters.toList()
        injectOrReplaceUnderTest(context, parameters)
        injectMissingConstructorParameters(context, parameters)
    }

    private fun injectMissingConstructorParameters(context: MockkInjectionContext, parameters: List<PsiParameter>) {
        parameters.reversed().forEach {
            insertConstructorParameter(context = context, parameter = it)
        }
    }

    private fun insertConstructorParameter(context: MockkInjectionContext, parameter: PsiParameter) {
        UnderTestParameterInjector.createUnderTestParameter(
            context = UnderTestParameterInstantiationContext(
                mockkContext = context,
                originalParameter = parameter,
                currentElement = parameter,
                parameterName = parameter.name,
            )
        )
    }

    private fun injectOrReplaceUnderTest(context: MockkInjectionContext, parameters: List<PsiParameter>) {
        val existingUnderTest = context.listCloseChildren()
            .filterIsInstance<KtProperty>()
            .firstOrNull { it.name == underTestVariableName }
        context.replaceElementWithStatement(
            elementToReplace = existingUnderTest,
            text = """val $underTestVariableName = ${context.testedClass.name}(
                    ${parameters.joinToString(separator = "\n") { "${it.name} = ${it.name}," }}
                )
                """
        )
    }
}
