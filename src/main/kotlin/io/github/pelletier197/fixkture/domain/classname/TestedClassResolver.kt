package io.github.pelletier197.fixkture.domain.classname

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiType
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTypesUtil

class TestedClassResolver {
    private val testClassSuffixes = listOf("Test", "IT", "IntegrationTest")

    fun isTestClass(testClass: PsiClass): Boolean {
        return resolveTestedClassName(testClass) != null
    }

    fun resolveTestedClassFromTestClass(testClass: PsiClass, project: Project): PsiClass? {
        val targetClassName = resolveTestedClassName(testClass) ?: return null
        val targetType = PsiType.getTypeByName(targetClassName, project, GlobalSearchScope.allScope(project))
        return PsiTypesUtil.getPsiClass(targetType)
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
