[![JetBrains IntelliJ plugins](https://img.shields.io/jetbrains/plugin/d/15520?logo=Intellij%20IDEA&style=for-the-badge)](https://plugins.jetbrains.com/plugin/15520-fixkture)
[![JetBrains IntelliJ Plugins](https://img.shields.io/jetbrains/plugin/v/15520?label=PLUGIN&logo=IntelliJ%20IDEA&style=for-the-badge)](https://plugins.jetbrains.com/plugin/15520-fixkture)
[![CircleCI](https://img.shields.io/circleci/build/github/pelletier197/Mockkator?label=Circle%20CI&logo=circleci&style=for-the-badge)](https://app.circleci.com/pipelines/github/pelletier197/Fixkture)

[comment]: <> (<p align="center">)

[comment]: <> (  <img src="./logo/logo.png">)

[comment]: <> (</p>)

# Mockkator
Mockkator is an Intellij plugin you can use to generate [Mockk](https://github.com/mockk/mockk) boilerplate code in your unit tests.

## Example
Given a sample class 
```kotlin
class ComplexService(
    val input: String,
    val otherObject: TestObject
) {
    // Your logic
}
```

The generated test code will look like this:
```kotlin
class ComplexServiceTest : ShouldSpec ({
    // Generated code begins here
    val input = "input"
    val otherObject = mockk<TestObject>()
    
    val underTest = ComplexService(
        input = input,
        otherObject = otherObject,
    )
})
```
> This example uses [Kotest](https://github.com/kotest/kotest) as the testing framework, but this plugin is agnostic of what test framework you use


## Usage
You can download the plugin from the [Plugin Marketplace](https://plugins.jetbrains.com/plugin/15520-fixkture) and follow the instructions detailed in the documentation of the plugin.

## Contribute
You can contribute to this project by opening your own PR or by creating issues to propose improvement ideas. 
