package pt.rafap.ktflag.style

/**
 * Simple ANSI color and style utilities for console output.
 *
 * Provides a set of constants for common colors and styles, as well as
 * [colorText] to compose multiple styles and wrap text with reset.
 */
object Colors {
    /** ANSI reset sequence to clear all styles. */
    const val RESET = "\u001B[0m"
    /** Red foreground color. */
    const val RED = "\u001B[31m"
    /** Green foreground color. */
    const val GREEN = "\u001B[32m"
    /** Yellow foreground color. */
    const val YELLOW = "\u001B[33m"
    /** Blue foreground color. */
    const val BLUE = "\u001B[34m"
    /** Purple (magenta) foreground color. */
    const val PURPLE = "\u001B[35m"
    /** Cyan foreground color. */
    const val CYAN = "\u001B[36m"
    /** White foreground color. */
    const val WHITE = "\u001B[37m"
    /** Bold style. */
    const val BOLD = "\u001B[1m"
    /** Underline style. */
    const val UNDERLINE = "\u001B[4m"

    /** Default color used for error messages. */
    const val ERROR_COLOR = RED
    /** Default color used for warnings. */
    const val WARNING_COLOR = GREEN
    /** Default color used for informational messages. */
    const val INFO_COLOR = GREEN
    /** Color used for help usage tokens. */
    const val HELP_USAGE_COLOR = CYAN
    /** Color used for help alias tokens. */
    const val HELP_ALIAS_COLOR = BLUE
    /** Color used for help descriptions. */
    const val HELP_DESC_COLOR = YELLOW
    /** Color used for prompt. */
    const val PROMPT_COLOR = PURPLE

    /**
     * Wraps [text] with one or more ANSI [colors] or styles and appends [RESET].
     *
     * Example: colorText("Hello", BOLD, GREEN)
     */
    fun colorText(text: String, vararg colors: String): String {
        val colorSeq = colors.joinToString("")
        return "$colorSeq$text$RESET"
    }
}