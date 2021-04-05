package io.github.pelletier197.mockkator.domain.classname

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiType
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTypesUtil
import io.github.pelletier197.mockkator.domain.mockk.exception.MockkCodeInjectionException
import java.lang.IllegalArgumentException

class TestedClassNotFound : MockkCodeInjectionException("could not resolve tested class from test class name")

class TestedClassResolver {
    private val testClassSuffixes = listOf("Test", "IT", "IntegrationTest")

    fun isTestClass(testClass: PsiClass): Boolean {
        return resolveTestedClassName(testClass) != null
    }

    fun resolveTestedClassFromTestClass(testClass: PsiClass, project: Project): PsiClass {
        val targetClassName = resolveTestedClassName(testClass) ?: throw IllegalArgumentException("target class ${testClass.name} is not a test class")
        val targetType = PsiType.getTypeByName(targetClassName, project, GlobalSearchScope.allScope(project))
        return PsiTypesUtil.getPsiClass(targetType) ?: throw TestedClassNotFound()
    }

    private fun resolveTestedClassName(psiClass: PsiClass): String? {
        testClassSuffixes.forEach {
            if (psiClass.qualifiedName?.endsWith(it) == true) {
                return psiClass.qualifiedName!!.substring(0, psiClass.qualifiedName!!.length - it.length)
            }
        }

        return null
    }
}
