# 🧱 KtFlag

[![](https://jitpack.io/v/RafaPear/KtFlag.svg)](https://jitpack.io/#RafaPear/KtFlag) [![Release Tests](https://img.shields.io/github/actions/workflow/status/RafaPear/KtFlag/release-tests.yml?event=release&label=release%20tests)](https://github.com/RafaPear/KtFlag/actions/workflows/release-tests.yml)

KtFlag is a small Kotlin library to build simple, extensible command-line interfaces (CLI) with commands, aliases, and colored help output.

## 🚀 Features
- Lightweight command contract (CommandImpl) with metadata (CommandInfo)
- Simple registry for dynamic command registration and alias resolution
- Built-in help command (help, h, ?) listing all registered commands
- Argument count validation helpers
- Colored and readable console output

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
    val registry = CommandRegister<Unit>() // comes with a built-in help command
    registry.registerCommands(Echo)

    // invoke the help
    registry["help"]?.execute(context = null)

    // run our command
    registry["echo"]?.execute("Hello, KtFlag!", context = null)
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

The resulting JAR is placed in `build\libs` (for example, `build\libs\KtFlag-1.0-Release.jar`).

Run the included example:

```bat
gradlew.bat runExample
```

## 📚 API documentation (Dokka 2.0)
Generate HTML docs locally with Dokka:

```bat
gradlew.bat dokkaHtml
```

Open `build\dokka\html\index.html` in your browser. The project already uses Dokka 2.0.

## ✨ Tips
- Use `CommandUtils.verifyArgsCount(count)` inside your command to validate argument count against `minArgs..maxArgs`.
- The `Colors` utility exposes simple ANSI colors to make help output readable. You can combine multiple styles: `Colors.colorText("text", Colors.BOLD, Colors.GREEN)`.

## 🤝 Contributions
Issues and PRs are welcome.

## 📄 License
MIT License.
