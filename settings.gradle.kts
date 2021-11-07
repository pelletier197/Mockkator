rootProject.name = "mockkator"

pluginManagement {
    val kotlinVersion: String by settings
    val ktlintVersion: String by settings
    val gradleIntellijVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion

        id("org.jlleitschuh.gradle.ktlint") version ktlintVersion
        id("org.jlleitschuh.gradle.ktlint-idea") version ktlintVersion

        id("org.jetbrains.intellij") version gradleIntellijVersion
    }
}
