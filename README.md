# 🧱 KtFlag

[![](https://jitpack.io/v/RafaPear/KtFlag.svg)](https://jitpack.io/#RafaPear/KtFlag) [![Release Tests](https://img.shields.io/github/actions/workflow/status/RafaPear/KtFlag/release-tests.yml?event=release&label=release%20tests)](https://github.com/RafaPear/KtFlag/actions/workflows/release-tests.yml)

A small, practical Kotlin library for wiring simple command line commands with aliases, minimal metadata, and colored help output. Nothing fancy—just a lightweight layer so you don't re‑write the same command parsing glue.

## 🔍 What it does (briefly)
- Lets you define commands with names, aliases, usage, and argument count
- Includes a built‑in help command (or supply your own)
- Provides colored, readable output helpers
- Gives you simple result types for success / errors / invalid args / unknown command

## 🚀 Features
- Lightweight command contract (`CommandImpl`) with metadata (`CommandInfo`)
- Simple registry for dynamic command lookup and fuzzy suggestions
- Built‑in help command (`help`, `h`, `?`) listing all registered commands
- Argument count validation helpers
- ANSI color utilities for help text (`Colors`)

## 🛠️ Quick start
Create your command by implementing `CommandImpl<T>` and exposing its `info`:

```kotlin
// Example command: echoes the first argument
object Echo : CommandImpl<Unit> {
    override val info = CommandInfo(
        title = "Echo",
        description = "Prints the provided text.",
        longDescription = "Echoes the first argument back to the console.",
        aliases = listOf("echo", "e"),
        usage = "echo <text>",
        minArgs = 1,
        maxArgs = 1
    )

    override fun execute(vararg arg: String, context: Unit?): CommandResult<Unit> {
        if (!verifyArgsCount(arg.size)) return INVALID_ARGS(info, arg.size)
        println(arg[0])
        return RESULT(Unit)
    }
}
```

Register commands and run them:

```kotlin
fun main() {
    val parser = CommandParser(Echo) // help command is auto-registered
    val result = parser.parseInputToResult("echo Hello", null)
    println(result?.message)
}
```

A minimal runnable example is available in `src/example/kotlin` and can be executed with the task `runExample`.

## 📥 Installation
### Using JitPack (Gradle)
Add JitPack to your repositories and declare the dependency:

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.RafaPear:KtFlag:VERSION'
}
```
Replace `VERSION` with the desired release (see badge above).

### From source
Windows (cmd.exe):

```bat
git clone https://github.com/RafaPear/KtFlag.git
cd KtFlag
gradlew.bat build
```

The resulting JAR is placed in `build\libs`.

Run the included example:

```bat
gradlew.bat runExample
```

## 🔄 Release test status
The SVG badge `release-status.svg` in the repo root is updated automatically on every GitHub Release (published or pre‑release) by the workflow `.github/workflows/release-tests.yml`:

1. Runs the test suite on Ubuntu with JDK 21
2. Generates a green (passing) or red (failing) badge
3. Commits the new `release-status.svg` back to the default branch

You can inspect historical runs here: https://github.com/RafaPear/KtFlag/actions/workflows/release-tests.yml

## 📚 API documentation (Dokka 2.0)
Generate HTML docs locally with Dokka:

```bat
gradlew.bat dokkaHtml
```

Open `build/dokka/html/index.html` in your browser. Most public classes & functions now have KDoc comments; feel free to improve wording via PR.

## ✨ Tips
- Use `verifyArgsCount(argCount)` inside your command to validate argument count.
- Combine styles: `Colors.colorText("Hi", Colors.BOLD, Colors.GREEN)`.
- Use `CommandParser.findSimilarCommands("ech")` to suggest alternatives.

## 🤝 Contributions
Issues and PRs are welcome. Keep things small and focused—this library intentionally stays minimal.

## 📄 License
MIT License.
