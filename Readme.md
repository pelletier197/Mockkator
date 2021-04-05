[![JetBrains IntelliJ plugins](https://img.shields.io/jetbrains/plugin/d/16471?logo=Intellij%20IDEA&style=for-the-badge)](https://plugins.jetbrains.com/plugin/16471-mockkator)
[![JetBrains IntelliJ Plugins](https://img.shields.io/jetbrains/plugin/v/16471?label=PLUGIN&logo=IntelliJ%20IDEA&style=for-the-badge)](https://plugins.jetbrains.com/plugin/16471-mockkator)
[![CircleCI](https://img.shields.io/circleci/build/github/pelletier197/Mockkator?label=Circle%20CI&logo=circleci&style=for-the-badge)](https://app.circleci.com/pipelines/github/pelletier197/Mockkator)

<p align="center">
  <img src="./logo/logo.png">
</p>

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
You can download the plugin from the [Plugin Marketplace](https://plugins.jetbrains.com/plugin/16471-mockkator) and follow the instructions detailed in the documentation of the plugin.

## Contribute
You can contribute to this project by forking it and opening your own PR or by creating issues to propose improvements or report bugs.