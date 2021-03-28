package io.github.pelletier197.fixkture.domain.mockk

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiType
import com.intellij.psi.codeStyle.JavaCodeStyleManager
import com.intellij.psi.codeStyle.VariableKind
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

    fun suggestVariableName(type: PsiType): String {
        return JavaCodeStyleManager.getInstance(project)
            .suggestVariableName(VariableKind.FIELD, null, null, type).names.first()
    }

    fun addImport(import: String) {
        val importToAdd = ktFactory.createImportDirective(ImportPath.fromString(import))
        val list = file.importList!!
        if (list.imports.any { it.importPath?.fqName?.asString() == import }) return
        list.add(importToAdd)
    }

    fun insertStatement(text: String) {
        val statement = ktFactory.createDeclaration<KtDeclaration>(text)
        element?.parent?.addBefore(statement, element) ?: file.add(statement)
    }
}
