package io.github.pelletier197.fixkture.domain.mockk

import com.intellij.psi.PsiParameter
import com.intellij.psi.util.PsiTypesUtil
import io.github.pelletier197.fixkture.domain.mockk.exception.MockkCodeInjectionException
import org.jetbrains.kotlin.psi.KtProperty

class NoPrimaryConstructorDefinedException :
    MockkCodeInjectionException("No primary constructor was found for your class")

class MockkCodeInjector {
    val underTestVariableName = "underTest"

    fun inject(context: MockkInjectionContext) {
        val targetConstructor = context.testedClass.primaryConstructor ?: throw NoPrimaryConstructorDefinedException()
        val parameters = targetConstructor.parameterList.parameters.toList()
        injectMissingConstructorParameters(context, parameters)
        injectOrReplaceUnderTest(context, parameters)
        println(parameters)
    }

    private fun injectMissingConstructorParameters(context: MockkInjectionContext, parameters: List<PsiParameter>) {
        parameters.forEach {
            insertConstructorParameter(context = context, parameter = it)
        }
    }

    private fun insertConstructorParameter(context: MockkInjectionContext, parameter: PsiParameter) {
        context.addImportIfNotExist("io.mockk.mockk")
        context.addImportIfNotExist(parameter.type.canonicalText)
        context.insertUnderTestParameter(
            text = "val ${parameter.name} = mockk<${PsiTypesUtil.getPsiClass(parameter.type)!!.name}>()"
        )
    }

    private fun injectOrReplaceUnderTest(context: MockkInjectionContext, parameters: List<PsiParameter>) {
        val existingUnderTest = context.element?.parent?.children
            .orEmpty()
            .filterIsInstance<KtProperty>()
            .firstOrNull { it.name == underTestVariableName }
        context.replaceElementWithStatement(
            elementToReplace = existingUnderTest,
            text = """val $underTestVariableName = ${context.testedClass.qualifiedName}(
                    ${parameters.joinToString(separator = "\n") { "${it.name} = ${it.name}," }}
                )
                """
        )
    }
}
