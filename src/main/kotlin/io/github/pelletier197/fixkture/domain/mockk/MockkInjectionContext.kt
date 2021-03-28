package io.github.pelletier197.fixkture.domain.mockk

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.KtDeclaration
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtPsiFactory
import org.jetbrains.kotlin.resolve.ImportPath

val PsiClass.primaryConstructor get() = this.constructors.firstOrNull()

data class MockkInjectionContext(
    val project: Project,
    val file: KtFile,
    val element: PsiElement?,
    val testClass: PsiClass,
    val testedClass: PsiClass,
) {
    private val ktFactory = KtPsiFactory(project)

    fun addImportIfNotExist(import: String) {
        val importToAdd = ktFactory.createImportDirective(ImportPath.fromString(import))
        val list = file.importList!!
        if (list.imports.any { it.importPath?.fqName?.asString() == import }) return
        list.add(importToAdd)
    }

    fun insertUnderTestParameter(text: String) {
        val existingStatements = element?.parent?.children.orEmpty().map { it.text }
        if (existingStatements.contains(text)) return
        val statement = ktFactory.createDeclaration<KtDeclaration>(text)
        element?.parent?.addBefore(statement, element) ?: file.add(statement)
    }

    fun replaceElementWithStatement(element: PsiElement?, text: String) {
        val statement = ktFactory.createDeclaration<KtDeclaration>(text)
        element?.replace(statement) ?: element?.parent?.addBefore(statement, element) ?: file.add(statement)
    }
}
