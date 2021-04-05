package io.github.pelletier197.mockkator.domain.mockk.generator

import com.intellij.psi.util.PsiTypesUtil
import io.github.pelletier197.mockkator.domain.mockk.generator.Utils.extractType

object MockkClassGenerator {
    fun generateMockk(context: UnderTestParameterInstantiationContext) {
        val type = extractType(context.currentElement)!!
        context.addImportIfNotExist("io.mockk.mockk")
        context.addImportIfNotExist(type.canonicalText)
        context.createParameterWithInstantiationDeclaration("mockk<${PsiTypesUtil.getPsiClass(type)!!.name}>()")
    }
}
