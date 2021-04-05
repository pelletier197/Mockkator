package io.github.pelletier197.mockkator.domain.mockk.generator

import com.intellij.psi.PsiType
import com.intellij.psi.PsiWildcardType
import com.intellij.psi.impl.source.PsiClassReferenceType
import com.intellij.psi.util.PsiUtil
import io.github.pelletier197.mockkator.domain.mockk.UnderTestParameterInjector.createUnderTestParameter
import io.github.pelletier197.mockkator.domain.mockk.generator.Utils.extractType

object CollectionGenerator {
    private val collectionEndingThatCanBeStripped = listOf("Set", "List", "s", "Array")

    fun generateList(context: UnderTestParameterInstantiationContext) {
        return createCollection("listOf(", context, ")")
    }

    fun generateArrayList(context: UnderTestParameterInstantiationContext) {
        return createCollection("ArrayList(listOf(", context, "))")
    }

    fun generateLinkedList(context: UnderTestParameterInstantiationContext) {
        return createCollection("LinkedList(listOf(", context, "))")
    }

    fun generateSet(context: UnderTestParameterInstantiationContext) {
        return createCollection("setOf(", context, ")")
    }

    fun generateHashset(context: UnderTestParameterInstantiationContext) {
        return createCollection("HashSet(setOf(", context, "))")
    }

    fun generateTreeSet(context: UnderTestParameterInstantiationContext) {
        return createCollection("TreeSet(setOf(", context, "))")
    }

    fun generateIterable(context: UnderTestParameterInstantiationContext) {
        return generateList(context)
    }

    fun generateMap(context: UnderTestParameterInstantiationContext) {
        createMap("mapOf(", context, ")")
    }

    fun generateHashMap(context: UnderTestParameterInstantiationContext) {
        createMap("HashMap(mapOf(", context, "))")
    }

    fun generateTreeMap(context: UnderTestParameterInstantiationContext) {
        createMap("TreeMap(mapOf(", context, "))")
    }

    private fun createCollection(
        beforeText: String,
        context: UnderTestParameterInstantiationContext,
        afterText: String
    ) {
        val targetType = getIterableElementType(context)!!
        val collectionElementName = generateCollectionElementName(context.parameterName)
        context.createParameterWithInstantiationDeclaration("$beforeText$collectionElementName$afterText")
        createCollectionArgument(context = context, name = collectionElementName, targetType = targetType)
    }

    private fun createMapKeyElement(context: UnderTestParameterInstantiationContext, name: String) {
        return createMapArgument(context, name, 0)
    }

    private fun createMapValueElement(context: UnderTestParameterInstantiationContext, name: String) {
        return createMapArgument(context, name, 1)
    }

    private fun createMapArgument(context: UnderTestParameterInstantiationContext, name: String, parameterIndex: Int) {
        val type = extractType(context.currentElement)!!

        if (type is PsiClassReferenceType) {
            val parameterTypes = type.reference.typeParameters
            return createCollectionArgument(context = context, name = name, targetType = parameterTypes[parameterIndex])
        }
    }

    private fun createCollectionArgument(
        context: UnderTestParameterInstantiationContext,
        name: String,
        targetType: PsiType
    ) {
        return createUnderTestParameter(
            context = context.createForSubVariable(
                parameterName = name,
                element = targetType,
            )
        )
    }

    private fun createMap(
        beforeText: String,
        context: UnderTestParameterInstantiationContext,
        afterText: String
    ) {
        val keyName = generateCollectionElementName(context.parameterName) + "Key"
        val valueName = generateCollectionElementName(context.parameterName) + "Value"
        context.createParameterWithInstantiationDeclaration("$beforeText$keyName to $valueName$afterText")
        createMapKeyElement(context, keyName)
        createMapValueElement(context, valueName)
    }

    private fun getIterableElementType(context: UnderTestParameterInstantiationContext): PsiType? {
        return extractType(context.currentElement)?.let { extractListElementTypeFromType(it) }
    }

    private fun extractListElementTypeFromType(element: PsiType): PsiType? {
        val parameterType = PsiUtil.extractIterableTypeParameter(element, false)

        if (parameterType is PsiWildcardType) {
            return parameterType.bound
        }

        return parameterType
    }

    fun generateCollectionElementName(collectionName: String): String {
        // Plural list name. Just remove the trailing `s` and return the element name
        collectionEndingThatCanBeStripped.forEach {
            if (collectionName.endsWith(it) && collectionName.length > it.length) {
                return collectionName.dropLast(it.length)
            }
        }

        return collectionName + "Element"
    }
}
