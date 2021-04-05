package io.github.pelletier197.mockkator.domain.mockk.generator

import com.intellij.psi.PsiType
import com.intellij.psi.PsiWildcardType
import com.intellij.psi.impl.source.PsiClassReferenceType
import com.intellij.psi.util.PsiUtil
import io.github.pelletier197.mockkator.domain.mockk.UnderTestParameterInjector.createInstantiationFieldIfPossible
import io.github.pelletier197.mockkator.domain.mockk.generator.Utils.extractType

object CollectionGenerator {
    private val collectionEndingThatCanBeStripped = listOf("Set", "List", "s")

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
        return LanguageCallbackInstantiationFieldBuilder(
            kotlin = { context ->
                "mapOf(${createMapKeyBuilder(context).asKotlinFlatValue(context)} to ${
                createMapValueBuilder(
                    context
                ).asKotlinFlatValue(context)
                })"
            }
        )
    }

    fun generateHashMap(context: UnderTestParameterInstantiationContext) {
        val mapBuilder = generateMap()
        return LanguageCallbackInstantiationFieldBuilder(
            kotlin = { context -> "HashMap(${mapBuilder.asKotlinFlatValue(context)})" }
        )
    }

    fun generateTreeMap(context: UnderTestParameterInstantiationContext) {
        val mapBuilder = generateMap()
        return LanguageCallbackInstantiationFieldBuilder(
            kotlin = { context -> "TreeMap(${mapBuilder.asKotlinFlatValue(context)})" }
        )
    }

    private fun createCollection(
        beforeText: String,
        context: UnderTestParameterInstantiationContext,
        afterText: String
    ) {
        val targetType = getIterableElementType(context)!!
        val collectionElementName = generateCollectionElementName(context.parameterName)
        context.createParameterWithInstantiationDeclaration("$beforeText$collectionElementName$afterText")
        createInstantiationFieldIfPossible(
            context = context.createForSubVariable(
                parameterName = collectionElementName,
                element = targetType,
            )
        )
    }

    private fun createMapKeyBuilder(context: UnderTestParameterInstantiationContext) {
        return createMapArgument(context, 0)
    }

    private fun createMapValueBuilder(context: UnderTestParameterInstantiationContext) {
        return createMapArgument(context, 1)
    }

    private fun createMapArgument(context: UnderTestParameterInstantiationContext, parameterIndex: Int) {
        val type = extractType(context.currentElement)!!

        if (type is PsiClassReferenceType) {
            val parameterTypes = type.reference.typeParameters
            return createArgument(context, parameterTypes[parameterIndex])
        }
    }

    private fun createCollectionArgument(context: UnderTestParameterInstantiationContext) {
        val targetType = getIterableElementType(context)!!
        return createArgument(context, targetType)
    }

    private fun createArgument(context: UnderTestParameterInstantiationContext, targetType: PsiType) {
        return
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

    private fun generateCollectionElementName(collectionName: String): String {
        // Plural list name. Just remove the trailing `s` and return the element name
        collectionEndingThatCanBeStripped.forEach {
            if (collectionName.endsWith(it) && collectionName.length > it.length) {
                return collectionName.dropLast(it.length)
            }
        }

        return collectionName + "Element"
    }
}
