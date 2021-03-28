package io.github.pelletier197.fixkture.domain.mockk

import io.github.pelletier197.fixkture.domain.mockk.exception.MockkCodeInjectionException

class NoPrimaryConstructorDefinedException :
    MockkCodeInjectionException("No primary constructor was found for your class")

class MockkCodeInjector {
    fun inject(context: MockkInjectionContext) {
        val targetConstructor = context.testedClass.primaryConstructor ?: throw NoPrimaryConstructorDefinedException()
        val parameters = targetConstructor.parameterList.parameters
        println(parameters)
    }
}
