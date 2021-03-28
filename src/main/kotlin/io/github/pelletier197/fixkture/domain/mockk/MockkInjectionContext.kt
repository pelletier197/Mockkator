package io.github.pelletier197.fixkture.domain.mockk

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass

val PsiClass.primaryConstructor get() = this.constructors.firstOrNull()

data class MockkInjectionContext(
    val project: Project,
    val testClass: PsiClass,
    val testedClass: PsiClass,
)
