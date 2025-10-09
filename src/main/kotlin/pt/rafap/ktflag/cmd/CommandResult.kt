package pt.rafap.ktflag.cmd

import pt.rafap.ktflag.style.Colors

/**
 * Represents the outcome of executing a command.
 *
 * A result contains a human-friendly [message], a
 * [type] categorising the outcome (success / error variants), and an optional
 * typed payload [result] supplied by successful executions.
 *
 * @param T Type of the optional payload returned by the command.
 * @property message Detailed message about the outcome.
 * @property type Kind of result; influences formatting and semantics.
 * @property result Optional payload returned by the command execution.
 */
@Suppress("unused")
open class CommandResult<T>(
    val message: String,
    val type: CommandResultType,
    val result: T? = null
) {

    /**
     * Prints a single colored line representing this result when it is
     * considered an error (any non-success [type]). Successes are also
     * printed with the same format for consistency.
     */
    fun printError() {
        println(Colors.colorText("${type.prefix}: $message", Colors.ERROR_COLOR))
    }

    /**
     * Creates a copy of the current [CommandResult] with optional overrides
     * for [message], [type], and [result].
     */
    fun CommandResult<T>.copy(
        message: String = this.message,
        type: CommandResultType = this.type,
        result: T? = this.result
    ): CommandResult<T> {
        return CommandResult(message, type, result)
    }

    /**
     * Prints a standardized message for an unknown command [name], hinting the
     * user to consult the provided help command [cmdHelp].
     */
    @Deprecated("This function requires a help cmd as an argument which implies some bad meeh logic")
    fun printUnknownCommand(name: String, cmdHelp: CommandImpl<*>) {
        println(Colors.colorText("Unknown command: $name", Colors.ERROR_COLOR))
        print(Colors.colorText("Use", Colors.INFO_COLOR))
        print(Colors.colorText(" ${cmdHelp.info.aliases} ", Colors.HELP_ALIAS_COLOR))
        println(Colors.colorText("to see the list of available commands.", Colors.INFO_COLOR))
    }

    class INVALID_ARGS<T>(info: CommandInfo, got: Int) : CommandResult<T>(
                message = "Argument count must be between ${info.minArgs} and ${info.maxArgs}, got $got.",
        type = CommandResultType.INVALID_ARGS,
        result = null
    )

    class UNKNOWN_COMMAND<T>(cmdName: String) : CommandResult<T>(
        message = "Command '$cmdName' was not found.",
        type = CommandResultType.UNKNOWN_COMMAND,
        result = null
    )

    class NOT_IMPLEMENTED<T>(feature: String) : CommandResult<T>(
        message = "Feature '$feature' is not implemented yet.",
        type = CommandResultType.NOT_IMPLEMENTED,
        result = null
    )

    class SUCCESS<T>(message: String, result: T? = null) : CommandResult<T>(
        message = message,
        type = CommandResultType.SUCCESS,
        result = result
    )

    class ERROR<T>(message: String) : CommandResult<T>(
        message = message,
        type = CommandResultType.ERROR,
        result = null
    )
}