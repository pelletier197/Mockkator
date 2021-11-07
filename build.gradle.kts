@file:Suppress("UsePropertyAccessSyntax")

import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.intellij")
    id("org.jlleitschuh.gradle.ktlint")
}

group = "io.github.pelletier197"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinJvmCompile> {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

intellij {
    version.set("2020.2.3")
    type.set("IC")
    plugins.set(listOf("java", "Kotlin"))
    updateSinceUntilBuild.set(false)
    pluginName.set("Mockkator")
}

tasks.withType<org.jetbrains.intellij.tasks.PublishPluginTask> {
    token.set(System.getenv("PUBLISH_TOKEN"))
    channels.set(listOf("Stable"))
}
