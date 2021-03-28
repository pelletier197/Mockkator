package io.github.pelletier197.fixkture.api

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.command.WriteCommandAction
import io.github.pelletier197.fixkture.domain.classname.TestedClassResolver
import io.github.pelletier197.fixkture.domain.mockk.MockkCodeInjector
import io.github.pelletier197.fixkture.domain.mockk.MockkInjectionContext

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
        }

        println("aaaa")
        // getGenerator(event.file)?.generateFixture(
        //     context = FixtureGenerationContext(
        //         project = event.currentProject,
        //         caret = event.caret,
        //         file = event.file
        //     )
        // )
    }

    // private fun getGenerator(file: PsiFile): FixtureGenerator? {
    //     if (file.isJava()) return JavaIntellijFixtureGenerator()
    //     if (file.isKotlin()) return KotlinIntellijFixtureGenerator()
    //     return null
    // }
}
