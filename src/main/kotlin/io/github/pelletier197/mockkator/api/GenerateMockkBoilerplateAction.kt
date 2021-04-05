package io.github.pelletier197.mockkator.api

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.command.WriteCommandAction
import io.github.pelletier197.mockkator.domain.classname.TestedClassResolver
import io.github.pelletier197.mockkator.domain.mockk.MockkCodeInjector
import io.github.pelletier197.mockkator.domain.mockk.MockkInjectionContext
import org.jetbrains.kotlin.idea.core.ShortenReferences

class GenerateMockkBoilerplateAction : AnAction() {
    private val testedClassResolver = TestedClassResolver()
    private val mockkCodeInjector = MockkCodeInjector()

    override fun update(event: AnActionEvent) {
        event.presentation.isEnabledAndVisible = event.project != null &&
            event.editorOrNull() != null &&
            event.fileOrNull() != null &&
            event.file.isSupported() &&
            event.parentClassElement != null &&
            testedClassResolver.isTestClass(event.parentClassElement!!)
    }

    override fun actionPerformed(event: AnActionEvent) {
        val testClass = event.parentClassElement!!
        val testedClass = testedClassResolver.resolveTestedClassFromTestClass(testClass, event.currentProject)

        WriteCommandAction.runWriteCommandAction(event.currentProject) {
            mockkCodeInjector.inject(
                context = MockkInjectionContext(
                    project = event.currentProject,
                    file = event.file,
                    testClass = testClass,
                    testedClass = testedClass,
                    element = event.currentElement,
                )
            )

            ShortenReferences.DEFAULT.process(event.file)
        }
    }
}
