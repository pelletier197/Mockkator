package io.github.pelletier197.fixkture.domain.mockk

import com.intellij.psi.PsiParameter
import com.intellij.psi.util.PsiTypesUtil
import io.github.pelletier197.fixkture.domain.mockk.exception.MockkCodeInjectionException

class NoPrimaryConstructorDefinedException :
    MockkCodeInjectionException("No primary constructor was found for your class")

class MockkCodeInjector {
    fun inject(context: MockkInjectionContext) {
        val targetConstructor = context.testedClass.primaryConstructor ?: throw NoPrimaryConstructorDefinedException()
        val parameters = targetConstructor.parameterList.parameters.toList()
        injectMissingConstructorParameters(context, parameters)
        println(parameters)
    }

    private fun injectMissingConstructorParameters(context: MockkInjectionContext, parameters: List<PsiParameter>) {
        parameters.forEach {
            insertConstructorParameter(context = context, parameter = it)
        }
    }

    private fun insertConstructorParameter(context: MockkInjectionContext, parameter: PsiParameter) {
        context.addImport("io.mockk.mockk")
        context.addImport(parameter.type.canonicalText)
        context.insertStatement(
            text = "val ${context.suggestVariableName(parameter.type)} = mockk<${PsiTypesUtil.getPsiClass(parameter.type)!!.name}>()"
        )
    }
}