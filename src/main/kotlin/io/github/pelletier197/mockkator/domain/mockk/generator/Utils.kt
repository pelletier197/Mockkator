package io.github.pelletier197.mockkator.domain.mockk.generator

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiParameter
import com.intellij.psi.PsiType

object Utils {
    fun extractType(element: PsiElement): PsiType? {
        return when (element) {
            is PsiParameter -> element.type
            is PsiType -> element
            else -> null
        }
    }
}
