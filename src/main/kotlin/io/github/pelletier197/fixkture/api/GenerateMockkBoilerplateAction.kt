package io.github.pelletier197.fixkture.api

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import io.github.pelletier197.fixkture.domain.classname.TestedClassResolver

class GenerateMockkBoilerplateAction : AnAction() {
    private val testedClassResolver = TestedClassResolver()

    override fun update(event: AnActionEvent) {
        event.presentation.isEnabledAndVisible = event.project != null &&
            event.editorOrNull() != null &&
            event.fileOrNull() != null &&
            event.file.isSupported() &&
            event.parentClassElement != null &&
            testedClassResolver.isTestClass(event.parentClassElement!!)
    }

    override fun actionPerformed(event: AnActionEvent) {
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
