package io.github.pelletier197.mockkator.domain.mockk

import com.intellij.psi.PsiArrayType
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiParameter
import com.intellij.psi.PsiPrimitiveType
import com.intellij.psi.PsiType
import com.intellij.psi.util.PsiUtil
import io.github.pelletier197.mockkator.domain.mockk.generator.EnumGenerator
import io.github.pelletier197.mockkator.domain.mockk.generator.LibraryGenerator
import io.github.pelletier197.mockkator.domain.mockk.generator.MockkClassGenerator
import io.github.pelletier197.mockkator.domain.mockk.generator.PrimitiveGenerator
import io.github.pelletier197.mockkator.domain.mockk.generator.TimeGenerator
import io.github.pelletier197.mockkator.domain.mockk.generator.UnderTestParameterInstantiationContext

object UnderTestParameterInjector {
    fun createInstantiationFieldIfPossible(context: UnderTestParameterInstantiationContext) {
        when (val element = context.currentElement) {
            is PsiClass -> handlePsiClass(element, context)
            is PsiParameter -> handlePsiParameter(element, context)
            is PsiType -> handlePsiType(element, context)
        }
    }

    private fun handlePsiClass(
        element: PsiClass,
        context: UnderTestParameterInstantiationContext,
    ) {
        when (element.qualifiedName) {
            // Primitives
            "Boolean", "java.lang.Boolean" -> PrimitiveGenerator.generateBoolean(context)
            "Integer", "java.lang.Integer" -> PrimitiveGenerator.generateInteger(context)
            "Long", "java.lang.Long" -> PrimitiveGenerator.generateLong(context)
            "Float", "java.lang.Float" -> PrimitiveGenerator.generateFloat(context)
            "Double", "java.lang.Double" -> PrimitiveGenerator.generateDouble(context)
            "Byte", "java.lang.Byte" -> PrimitiveGenerator.generateByte(context)
            "Char", "java.lang.Character" -> PrimitiveGenerator.generateChar(context)
            "String", "java.lang.String" -> PrimitiveGenerator.generateString(context)
            // Time
            "Instant", "java.time.Instant" -> TimeGenerator.generateInstant(context)
            "ZonedDateTime", "java.time.ZonedDateTime" -> TimeGenerator.generateZoneDateTime(context)
            "ZoneId", "java.time.ZoneId" -> TimeGenerator.generateZoneId(context)
            "LocalDate", "java.time.LocalDate" -> TimeGenerator.generateLocalDate(context)
            "LocalDateTime", "java.time.LocalDateTime" -> TimeGenerator.generateLocalDateTime(context)
            "LocalTime", "java.time.LocalTime" -> TimeGenerator.generateLocalTime(context)
            "Period", "java.time.Period" -> TimeGenerator.generatePeriod(context)
            // Other
            "UUID", "java.util.UUID" -> LibraryGenerator.generateUUID(context)
            "BigDecimal", "java.math.BigDecimal" -> LibraryGenerator.generateBigDecimal(context)
            "BigInteger", "java.math.BigInteger" -> LibraryGenerator.generateBigInteger(context)
            // Collections
            "List", "java.util.List" -> JavaCollectionGenerator.generateList()
            "Set", "java.util.Set" -> JavaCollectionGenerator.generateSet()
            "HashSet", "java.util.HashSet" -> JavaCollectionGenerator.generateHashset()
            "TreeSet", "java.util.TreeSet" -> JavaCollectionGenerator.generateTreeSet()
            "ArrayList", "java.util.ArrayList" -> JavaCollectionGenerator.generateArrayList()
            "LinkedList", "java.util.LinkedList" -> JavaCollectionGenerator.generateLinkedList()
            "Iterable", "java.lang.Iterable" -> JavaCollectionGenerator.generateIterable()
            "Map", "java.util.Map" -> JavaCollectionGenerator.generateMap()
            "HashMap", "java.util.HashMap" -> JavaCollectionGenerator.generateHashMap()
            "TreeMap", "java.util.TreeMap" -> JavaCollectionGenerator.generateTreeMap()
            else -> when (element.isEnum) {
                true -> EnumGenerator.generateEnum(context)
                else -> MockkClassGenerator.generateMockk(context)
            }
        }
    }

    fun handlePsiType(
        type: PsiType,
        context: UnderTestParameterInstantiationContext,
    ) {
        when (type) {
            is PsiPrimitiveType -> when (type.name) {
                "int" -> PrimitiveGenerator.generateInteger(context)
                "float" -> PrimitiveGenerator.generateFloat(context)
                "double" -> PrimitiveGenerator.generateDouble(context)
                "long" -> PrimitiveGenerator.generateLong(context)
                "char" -> PrimitiveGenerator.generateChar(context)
                "short" -> PrimitiveGenerator.generateByte(context)
                "boolean" -> PrimitiveGenerator.generateBoolean(context)
                "byte" -> PrimitiveGenerator.generateByte(context)
            }
            is PsiArrayType -> JavaArrayGenerator.generateArray()
            else -> PsiUtil.resolveClassInType(type)?.let { handlePsiClass(it, context) }
        }
    }

    private fun handlePsiParameter(
        parameter: PsiParameter,
        context: UnderTestParameterInstantiationContext,
    ) {
        return handlePsiType(parameter.type, context)
    }
}
