package io.github.pelletier197.mockkator.domain.mockk

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.KtDeclaration
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtPsiFactory
import org.jetbrains.kotlin.resolve.ImportPath

val PsiClass.primaryConstructor get() = this.constructors.firstOrNull()

data class MockkInjectionContext(
    val project: Project,
    val file: KtFile,
    var element: PsiElement?,
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
        val assignation = text.split("=").firstOrNull()?.trim()
        val existingStatements = listCloseChildren().map { it.text }
        if (assignation != null && existingStatements.any { it.startsWith("$assignation =") }) return
        val statement = ktFactory.createDeclaration<KtDeclaration>(text)
        element = element?.parent?.addBefore(statement, element) ?: file.add(statement)
    }

    fun replaceElementWithStatement(elementToReplace: PsiElement?, text: String) {
        val statement = ktFactory.createDeclaration<KtDeclaration>(text)
        element = elementToReplace?.replace(statement) ?: element?.parent?.addBefore(statement, element) ?: file.add(
            statement
        )
    }

    fun listCloseChildren(): List<PsiElement> {
        return listChildElements(element?.parent)
    }

    private fun listChildElements(element: PsiElement?): List<PsiElement> {
        if (element == null) return emptyList()
        if (element is KtProperty) return listOf(element)
        return element.children.flatMap { listChildElements(it) }
    }
}
